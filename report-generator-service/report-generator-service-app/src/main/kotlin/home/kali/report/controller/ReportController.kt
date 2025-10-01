package home.kali.report.controller

import home.kali.report.generated.api.ReportGenerationApi
import home.kali.report.generated.model.GenerateReportRequest
import home.kali.report.generated.model.GenerateReportResponse
import home.kali.report.generated.model.ReportStatusResponse
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class ReportController : ReportGenerationApi {

    override fun generateReport(generateReportRequest: GenerateReportRequest): ResponseEntity<GenerateReportResponse> {
        TODO("Implement generateReport")
    }

    override fun getReportStatus(reportId: UUID): ResponseEntity<ReportStatusResponse> {
        TODO("Implement getReportStatus")
    }

    override fun cancelReport(reportId: UUID): ResponseEntity<ReportStatusResponse> {
        TODO("Implement cancelReport")
    }

    override fun downloadReport(reportId: UUID, format: String): ResponseEntity<Resource> {
        TODO("Implement downloadReport")
    }
} 