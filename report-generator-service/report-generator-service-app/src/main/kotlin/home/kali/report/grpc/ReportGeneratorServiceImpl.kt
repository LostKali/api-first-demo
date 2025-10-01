package home.kali.report.grpc

import home.kali.report.grpc.*
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.stereotype.Service

@GrpcService
@Service
class ReportGeneratorServiceImpl : ReportGeneratorServiceGrpc.ReportGeneratorServiceImplBase() {

    override fun generateReport(
        request: GenerateReportRequest,
        responseObserver: StreamObserver<GenerateReportResponse>
    ) {
        val response = GenerateReportResponse.newBuilder()
            .setReportId(request.reportId)
            .setStatus(ReportStatus.PENDING)
            .setMessage("Report generation started")
            .build()
        
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    override fun getReportStatus(
        request: GetReportStatusRequest,
        responseObserver: StreamObserver<ReportStatusResponse>
    ) {
        val response = ReportStatusResponse.newBuilder()
            .setReportId(request.reportId)
            .setStatus(ReportStatus.COMPLETED)
            .setProgress(100)
            .setCurrentStep("Report ready for download")
            .setEstimatedTimeRemainingSeconds(0)
            .setFileSizeBytes(1024)
            .setRowCount(50)
            .build()
        
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    override fun cancelReport(
        request: CancelReportRequest,
        responseObserver: StreamObserver<ReportStatusResponse>
    ) {
        val response = ReportStatusResponse.newBuilder()
            .setReportId(request.reportId)
            .setStatus(ReportStatus.CANCELLED)
            .setProgress(0)
            .setCurrentStep("Report cancelled by user")
            .setEstimatedTimeRemainingSeconds(0)
            .setErrorMessage("Cancelled by user request")
            .build()
        
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    override fun downloadReport(
        request: DownloadReportRequest,
        responseObserver: StreamObserver<DownloadReportResponse>
    ) {
        val response = DownloadReportResponse.newBuilder()
            .setReportId(request.reportId)
            .setFileName("user_activity_report_2024-01.csv")
            .setContentType("text/csv")
            .setFileSizeBytes(2048)
            .build()
        
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
