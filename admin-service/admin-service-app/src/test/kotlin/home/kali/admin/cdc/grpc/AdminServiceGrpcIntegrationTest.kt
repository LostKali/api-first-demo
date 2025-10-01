package home.kali.admin.grpc

import home.kali.report.grpc.GenerateReportRequest
import home.kali.report.grpc.ReportStatus
import home.kali.report.grpc.ReportType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(
    properties = [
        "grpc.report-generator.host=localhost",
        "grpc.report-generator.port=9090"
    ]
)
class AdminServiceGrpcIntegrationTest {

    @Autowired
    private lateinit var reportGeneratorService: ReportGeneratorService

    @Test
    @Disabled("Integration test - requires running Report Generator Service on port 9090")
    fun testAdminServiceGrpcInteraction() {
        assertNotNull(reportGeneratorService)

        // Create a real gRPC request
        val request = GenerateReportRequest.newBuilder()
            .setReportId("admin-integration-test-1")
            .setDateFrom("2024-01-01")
            .setDateTo("2024-01-31")
            .setReportType(ReportType.USER_ACTIVITY)
            .setDescription("Admin service integration test")
            .build()

        // Make actual gRPC call
        val response = reportGeneratorService.generateReport(request)

        // Verify response
        assertNotNull(response)
        assertEquals("admin-integration-test-1", response.reportId)
        assertEquals(ReportStatus.PENDING, response.status)
        assertTrue(response.message.contains("Report generation started"))
    }
}
