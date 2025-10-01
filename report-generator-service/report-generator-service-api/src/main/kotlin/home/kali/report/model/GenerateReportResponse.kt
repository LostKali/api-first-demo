package home.kali.report.model

import java.time.OffsetDateTime
import java.util.UUID

data class GenerateReportResponse(
    val reportId: UUID,
    val status: ReportStatus,
    val estimatedCompletionTime: OffsetDateTime,
    val message: String
)

enum class ReportStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    CANCELLED
}
