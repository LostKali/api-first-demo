package home.kali.report.controller

import home.kali.report.api.InternalReportGeneratorApi
import home.kali.report.model.GenerateReportRequest
import home.kali.report.model.GenerateReportResponse
import home.kali.report.model.ReportStatusResponse
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class InternalReportController : InternalReportGeneratorApi {

    override fun generateReport(request: GenerateReportRequest): GenerateReportResponse {
        TODO("Implement generateReport")
    }

    override fun getReportStatus(reportId: UUID): ReportStatusResponse {
        TODO("Implement getReportStatus")
    }

    override fun cancelReport(reportId: UUID): ReportStatusResponse {
        TODO("Implement cancelReport")
    }

    override fun downloadReport(reportId: UUID, format: String): ByteArray {
        TODO("Implement downloadReport")
    }
}
