package home.kali.admin.controller

import home.kali.admin.generated.api.ReportsApi
import home.kali.admin.generated.model.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class AdminController : ReportsApi {

    override fun createReportRequest(createReportRequest: CreateReportRequest): ResponseEntity<ReportRequest> {
        TODO("Implement createReportRequest")
    }

    override fun getReports(status: ReportStatus?, page: Int, size: Int): ResponseEntity<ReportListResponse> {
        TODO("Implement getReports")
    }

    override fun getReport(reportId: UUID): ResponseEntity<ReportRequest> {
        TODO("Implement getReport")
    }

    override fun cancelReport(reportId: UUID): ResponseEntity<ReportRequest> {
        TODO("Implement cancelReport")
    }

    override fun downloadReport(reportId: UUID, format: String): ResponseEntity<org.springframework.core.io.Resource> {
        TODO("Implement downloadReport")
    }
} 