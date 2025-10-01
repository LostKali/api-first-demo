package home.kali.report.model

import java.util.UUID

data class ReportStatusResponse(
    val reportId: UUID,
    val status: ReportStatus,
    val progress: Int,
    val currentStep: String? = null,
    val estimatedTimeRemaining: Int? = null,
    val errorMessage: String? = null,
    val fileSize: Int? = null,
    val rowCount: Int? = null
)
