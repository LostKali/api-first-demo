package home.kali.report.model

import java.time.LocalDate
import java.util.UUID

data class GenerateReportRequest(
    val reportId: UUID,
    val dateFrom: LocalDate,
    val dateTo: LocalDate,
    val reportType: ReportType,
    val priority: Priority = Priority.NORMAL,
    val description: String? = null
)

enum class ReportType {
    USER_ACTIVITY,
    SALES_ANALYSIS,
    SYSTEM_METRICS
}

enum class Priority {
    LOW,
    NORMAL,
    HIGH,
    URGENT
}
