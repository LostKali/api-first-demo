package home.kali.report.api

import home.kali.report.model.GenerateReportRequest
import home.kali.report.model.GenerateReportResponse
import home.kali.report.model.ReportStatusResponse
import io.swagger.v3.oas.annotations.parameters.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange
import java.util.UUID

@HttpExchange("/api/internal/report")
interface InternalReportGeneratorApi {
    @PostExchange("/generate")
    fun generateReport(@RequestBody request: GenerateReportRequest): GenerateReportResponse

    @GetExchange("/{reportId}/status")
    fun getReportStatus(@PathVariable reportId: UUID): ReportStatusResponse

    @PostExchange("/{reportId}/cancel")
    fun cancelReport(@PathVariable reportId: UUID): ReportStatusResponse

    @GetExchange("/{reportId}/download")
    fun downloadReport(
        @PathVariable reportId: UUID,
        @RequestParam(defaultValue = "csv") format: String
    ): ByteArray
}
