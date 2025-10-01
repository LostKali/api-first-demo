package home.kali.admin.grpc

import home.kali.report.grpc.*
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.stereotype.Service

/**
 * gRPC client service for communicating with Report Generator Service
 */
@Service
class ReportGeneratorService(
    private val channelFactory: ChannelFactory
) {
    
    private val stub: ReportGeneratorServiceGrpc.ReportGeneratorServiceBlockingStub by lazy {
        ReportGeneratorServiceGrpc.newBlockingStub(channelFactory.createChannel())
    }

    fun generateReport(request: GenerateReportRequest): GenerateReportResponse {
        return stub.generateReport(request)
    }

    fun getReportStatus(request: GetReportStatusRequest): ReportStatusResponse {
        return stub.getReportStatus(request)
    }

    fun cancelReport(request: CancelReportRequest): ReportStatusResponse {
        return stub.cancelReport(request)
    }

    fun downloadReport(request: DownloadReportRequest): DownloadReportResponse {
        return stub.downloadReport(request)
    }
}
