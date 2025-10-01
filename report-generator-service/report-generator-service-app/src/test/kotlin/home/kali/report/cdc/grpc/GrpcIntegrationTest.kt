package home.kali.report.grpc

import home.kali.report.grpc.*
import io.grpc.ManagedChannelBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.slf4j.LoggerFactory

class GrpcIntegrationTest {

    private val logger = LoggerFactory.getLogger(GrpcIntegrationTest::class.java)

    @Test
    @Disabled("Integration test - requires running Report Generator Service on port 9090")
    fun testGrpcInteraction() {
        logger.info("Testing gRPC interaction...")
        
        val channel = ManagedChannelBuilder.forAddress("localhost", 9090)
            .usePlaintext()
            .build()
        
        try {
            val stub = ReportGeneratorServiceGrpc.newBlockingStub(channel)
            
            // Test generateReport
            val request = GenerateReportRequest.newBuilder()
                .setReportId("test-grpc-123")
                .setDateFrom("2024-01-01")
                .setDateTo("2024-01-31")
                .setReportType(ReportType.USER_ACTIVITY)
                .setDescription("Test gRPC interaction")
                .build()
            
            logger.info("Sending gRPC request: {}", request)
            val response = stub.generateReport(request)
            logger.info("Received gRPC response: {}", response)
            
            // Verify response
            assertEquals("test-grpc-123", response.reportId)
            assertEquals(ReportStatus.PENDING, response.status)
            assertEquals("Report generation started", response.message)
            
            // Test getReportStatus
            val statusRequest = GetReportStatusRequest.newBuilder()
                .setReportId("test-grpc-123")
                .build()
            
            logger.info("Sending status request: {}", statusRequest)
            val statusResponse = stub.getReportStatus(statusRequest)
            logger.info("Received status response: {}", statusResponse)
            
            // Verify status response
            assertEquals("test-grpc-123", statusResponse.reportId)
            assertEquals(ReportStatus.COMPLETED, statusResponse.status)
            assertEquals(100, statusResponse.progress)
            
            logger.info("gRPC interaction test completed successfully!")
            
        } catch (e: Exception) {
            logger.error("gRPC interaction test failed: {}", e.message, e)
            fail("gRPC interaction test failed: ${e.message}")
        } finally {
            channel.shutdown()
        }
    }
}
