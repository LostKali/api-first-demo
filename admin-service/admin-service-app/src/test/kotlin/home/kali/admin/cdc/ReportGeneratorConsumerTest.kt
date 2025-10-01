package home.kali.admin

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.PactSpecVersion
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import home.kali.admin.grpc.ChannelFactory
import home.kali.admin.grpc.GrpcHttpChannel
import home.kali.admin.grpc.ReportGeneratorService
import home.kali.report.grpc.*
import io.grpc.Channel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(PactConsumerTestExt::class)
@PactTestFor(
    providerName = "ReportGeneratorService",
    hostInterface = "localhost",
    pactVersion = PactSpecVersion.V3
)
class ReportGeneratorConsumerTest {

    // --- 1. Contract for GenerateReport ---
    @Pact(consumer = "AdminService", provider = "ReportGeneratorService")
    fun generateReportPact(builder: PactDslWithProvider): RequestResponsePact {
        return builder
            .given("report_generation_requested")
            .uponReceiving("generate report request")
            .path("/grpc/home.kali.report.ReportGeneratorService/GenerateReport")
            .method("POST")
            .headers(mapOf("Content-Type" to "application/json"))
            .body(
                """
                    {
                        "reportId": "test-report-123",
                        "dateFrom": "2024-01-01",
                        "dateTo": "2024-01-31",
                        "reportType": "USER_ACTIVITY",
                        "description": "Test report generation"
                    }
                """.trimIndent()
            )
            .willRespondWith()
            .status(200)
            .headers(mapOf("Content-Type" to "application/json"))
            .body(
                """
                    {
                        "reportId": "test-report-123",
                        "status": "PENDING",
                        "estimatedCompletionTime": "2024-01-15T11:00:00Z",
                        "message": "Report generation started"
                    }
                """.trimIndent()
            )
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "generateReportPact")
    fun testGenerateReport(mockServer: MockServer) {
        val reportGeneratorService = ReportGeneratorService(object : ChannelFactory {
            override fun createChannel(): Channel {
                return GrpcHttpChannel("localhost", mockServer.getPort())
            }
        })

        val request = GenerateReportRequest.newBuilder()
            .setReportId("test-report-123")
            .setDateFrom("2024-01-01")
            .setDateTo("2024-01-31")
            .setReportType(ReportType.USER_ACTIVITY)
            .setDescription("Test report generation")
            .build()

        val response = reportGeneratorService.generateReport(request)

        assertEquals("test-report-123", response.reportId)
        assertEquals(ReportStatus.PENDING, response.status)
        assertEquals("Report generation started", response.message)
        // Note: estimatedCompletionTime is optional and may not be set in mock response
    }

    // --- 2. Contract for GetReportStatus ---
    @Pact(consumer = "AdminService", provider = "ReportGeneratorService")
    fun getReportStatusPact(builder: PactDslWithProvider): RequestResponsePact {
        return builder
            .given("report_exists")
            .uponReceiving("get report status request")
            .path("/grpc/home.kali.report.ReportGeneratorService/GetReportStatus")
            .method("POST")
            .headers(mapOf("Content-Type" to "application/json"))
            .body(
                """
                    {
                        "reportId": "test-report-123"
                    }
                """.trimIndent()
            )
            .willRespondWith()
            .status(200)
            .headers(mapOf("Content-Type" to "application/json"))
            .body(
                """
                    {
                        "reportId": "test-report-123",
                        "status": "COMPLETED",
                        "progress": 100,
                        "currentStep": "Report ready for download",
                        "estimatedTimeRemainingSeconds": 0,
                        "fileSizeBytes": 1024,
                        "rowCount": 50
                    }
                """.trimIndent()
            )
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "getReportStatusPact")
    fun testGetReportStatus(mockServer: MockServer) {
        val reportGeneratorService = ReportGeneratorService(object : ChannelFactory {
            override fun createChannel(): Channel {
                return GrpcHttpChannel("localhost", mockServer.getPort())
            }
        })

        val request = GetReportStatusRequest.newBuilder()
            .setReportId("test-report-123")
            .build()

        val response = reportGeneratorService.getReportStatus(request)

        assertEquals("test-report-123", response.reportId)
        assertEquals(ReportStatus.COMPLETED, response.status)
        assertEquals(100, response.progress)
        assertEquals("Report ready for download", response.currentStep)
    }

    // --- 3. Contract for CancelReport ---
    @Pact(consumer = "AdminService", provider = "ReportGeneratorService")
    fun cancelReportPact(builder: PactDslWithProvider): RequestResponsePact {
        return builder
            .given("report_in_progress")
            .uponReceiving("cancel report request")
            .path("/grpc/home.kali.report.ReportGeneratorService/CancelReport")
            .method("POST")
            .headers(mapOf("Content-Type" to "application/json"))
            .body(
                """
                    {
                        "reportId": "test-report-123",
                        "reason": "User requested cancellation"
                    }
                """.trimIndent()
            )
            .willRespondWith()
            .status(200)
            .headers(mapOf("Content-Type" to "application/json"))
            .body(
                """
                    {
                        "reportId": "test-report-123",
                        "status": "CANCELLED",
                        "progress": 0,
                        "currentStep": "Report cancelled by user",
                        "estimatedTimeRemainingSeconds": 0,
                        "errorMessage": "Cancelled by user request"
                    }
                """.trimIndent()
            )
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "cancelReportPact")
    fun testCancelReport(mockServer: MockServer) {
        val reportGeneratorService = ReportGeneratorService(object : ChannelFactory {
            override fun createChannel(): Channel {
                return GrpcHttpChannel("localhost", mockServer.getPort())
            }
        })

        val request = CancelReportRequest.newBuilder()
            .setReportId("test-report-123")
            .setReason("User requested cancellation")
            .build()

        val response = reportGeneratorService.cancelReport(request)

        assertEquals("test-report-123", response.reportId)
        assertEquals(ReportStatus.CANCELLED, response.status)
        assertEquals("Report cancelled by user", response.currentStep)
        assertEquals("Cancelled by user request", response.errorMessage)
    }

    // --- 4. Contract for DownloadReport ---
    @Pact(consumer = "AdminService", provider = "ReportGeneratorService")
    fun downloadReportPact(builder: PactDslWithProvider): RequestResponsePact {
        return builder
            .given("report_completed")
            .uponReceiving("download report request")
            .path("/grpc/home.kali.report.ReportGeneratorService/DownloadReport")
            .method("POST")
            .headers(mapOf("Content-Type" to "application/json"))
            .body(
                """
                    {
                        "reportId": "test-report-123",
                        "format": "CSV"
                    }
                """.trimIndent()
            )
            .willRespondWith()
            .status(200)
            .headers(mapOf("Content-Type" to "application/json"))
            .body(
                """
                    {
                        "reportId": "test-report-123",
                        "fileContent": "aWRfZGF0ZV91c2VyX2FjdGlvbgoxLDIwMjQtMDEtMDEsdXNlcjEsbG9naW4KMiwyMDI0LTAxLTAyLHVzZXIyLGxvZ291dAo=",
                        "fileName": "user_activity_report_2024-01.csv",
                        "contentType": "text/csv",
                        "fileSizeBytes": 2048
                    }
                """.trimIndent()
            )
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "downloadReportPact")
    fun testDownloadReport(mockServer: MockServer) {
        val reportGeneratorService = ReportGeneratorService(object : ChannelFactory {
            override fun createChannel(): Channel {
                return GrpcHttpChannel("localhost", mockServer.getPort())
            }
        })

        val request = DownloadReportRequest.newBuilder()
            .setReportId("test-report-123")
            .setFormat(ReportFormat.CSV)
            .build()

        val response = reportGeneratorService.downloadReport(request)

        assertEquals("test-report-123", response.reportId)
        assertEquals("user_activity_report_2024-01.csv", response.fileName)
        assertEquals("text/csv", response.contentType)
        assertEquals(2048L, response.fileSizeBytes)
        // Verify file content is present (base64 encoded CSV data)
        assert(response.fileContent.size() > 0)
    }
}