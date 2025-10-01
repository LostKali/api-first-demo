package home.kali.report.service

import home.kali.report.grpc.*
import org.springframework.stereotype.Service

@Service
class ReportService {

    fun generateReport(request: GenerateReportRequest): GenerateReportResponse {
        return GenerateReportResponse.newBuilder()
            .setReportId(request.reportId)
            .setStatus(ReportStatus.PENDING)
            .setMessage("Report generation started")
            .setEstimatedCompletionTime(
                com.google.protobuf.Timestamp.newBuilder()
                    .setSeconds(1705316400) // 2024-01-15T11:00:00Z
                    .build()
            )
            .build()
    }

    fun getReportStatus(request: GetReportStatusRequest): ReportStatusResponse {
        return ReportStatusResponse.newBuilder()
            .setReportId(request.reportId)
            .setStatus(ReportStatus.COMPLETED)
            .setProgress(100)
            .setCurrentStep("Report ready for download")
            .setEstimatedTimeRemainingSeconds(0)
            .setFileSizeBytes(1024L)
            .setRowCount(50)
            .build()
    }

    fun cancelReport(request: CancelReportRequest): ReportStatusResponse {
        return ReportStatusResponse.newBuilder()
            .setReportId(request.reportId)
            .setStatus(ReportStatus.CANCELLED)
            .setProgress(0)
            .setCurrentStep("Report cancelled by user")
            .setEstimatedTimeRemainingSeconds(0)
            .setErrorMessage("Cancelled by user request")
            .build()
    }

    fun downloadReport(request: DownloadReportRequest): DownloadReportResponse {
        return DownloadReportResponse.newBuilder()
            .setReportId(request.reportId)
            .setFileName("user_activity_report_2024-01.csv")
            .setContentType("text/csv")
            .setFileSizeBytes(2048L)
            .setFileContent(
                com.google.protobuf.ByteString.copyFrom(
                    java.util.Base64.getDecoder().decode("aWRfZGF0ZV91c2VyX2FjdGlvbgoxLDIwMjQtMDEtMDEsdXNlcjEsbG9naW4KMiwyMDI0LTAxLTAyLHVzZXIyLGxvZ291dAo=")
                )
            )
            .build()
    }
}
