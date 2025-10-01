package home.kali.admin.controller

import home.kali.admin.generated.api.ReportsApi
import home.kali.admin.generated.model.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

@RestController
class AdminController : ReportsApi {

    @PostMapping("/pact/stateChange")
    fun stateChange(@RequestBody stateChange: Map<String, Any>): ResponseEntity<Map<String, String>> {
        // Pact state change endpoint for provider verification
        return ResponseEntity.ok(mapOf("status" to "ok"))
    }

    override fun createReportRequest(createReportRequest: CreateReportRequest): ResponseEntity<ReportRequest> {
        val report = ReportRequest(
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            dateFrom = createReportRequest.dateFrom,
            dateTo = createReportRequest.dateTo,
            reportType = ReportRequest.ReportType.valueOf(createReportRequest.reportType.value),
            status = ReportStatus.PENDING,
            priority = ReportRequest.Priority.valueOf(createReportRequest.priority?.value ?: "NORMAL"),
            description = createReportRequest.description,
            createdAt = OffsetDateTime.parse("2024-01-15T10:30:00Z"),
            updatedAt = OffsetDateTime.parse("2024-01-15T10:30:00Z")
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(report)
    }

    override fun getReports(status: ReportStatus?, page: Int, size: Int): ResponseEntity<ReportListResponse> {
        val report = ReportRequest(
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            dateFrom = LocalDate.of(2024, 1, 1),
            dateTo = LocalDate.of(2024, 1, 31),
            reportType = ReportRequest.ReportType.USER_ACTIVITY,
            status = ReportStatus.COMPLETED,
            priority = ReportRequest.Priority.NORMAL,
            description = "USER_ACTIVITY report",
            createdAt = OffsetDateTime.parse("2024-01-15T10:30:00Z"),
            updatedAt = OffsetDateTime.parse("2024-01-15T11:30:00Z")
        )
        
        return ResponseEntity.ok(ReportListResponse(
            content = listOf(report),
            totalElements = 1,
            totalPages = 1,
            currentPage = page,
            pageSize = size
        ))
    }

    override fun getReport(reportId: UUID): ResponseEntity<ReportRequest> {
        val report = ReportRequest(
            id = reportId,
            dateFrom = LocalDate.of(2024, 1, 1),
            dateTo = LocalDate.of(2024, 1, 31),
            reportType = ReportRequest.ReportType.USER_ACTIVITY,
            status = ReportStatus.PROCESSING,
            priority = ReportRequest.Priority.NORMAL,
            description = "USER_ACTIVITY report",
            createdAt = OffsetDateTime.parse("2024-01-15T10:30:00Z"),
            updatedAt = OffsetDateTime.parse("2024-01-15T10:35:00Z"),
            progress = 75
        )
        return ResponseEntity.ok(report)
    }

    override fun cancelReport(reportId: UUID): ResponseEntity<ReportRequest> {
        val report = ReportRequest(
            id = reportId,
            dateFrom = LocalDate.of(2024, 1, 1),
            dateTo = LocalDate.of(2024, 1, 31),
            reportType = ReportRequest.ReportType.USER_ACTIVITY,
            status = ReportStatus.CANCELLED,
            priority = ReportRequest.Priority.NORMAL,
            description = "USER_ACTIVITY report",
            createdAt = OffsetDateTime.parse("2024-01-15T10:30:00Z"),
            updatedAt = OffsetDateTime.parse("2024-01-15T10:40:00Z")
        )
        return ResponseEntity.ok(report)
    }

    override fun downloadReport(reportId: UUID, format: String): ResponseEntity<org.springframework.core.io.Resource> {
        val content = "id,date,user,action\n1,2024-01-01,user1,login\n2,2024-01-02,user2,logout"
        val resource = org.springframework.core.io.ByteArrayResource(content.toByteArray())
        
        return ResponseEntity.ok()
            .header("Content-Type", "application/octet-stream")
            .header("Content-Disposition", "attachment; filename=report.csv")
            .body(resource)
    }
} 