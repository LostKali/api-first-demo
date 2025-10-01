package home.kali.report.grpc

import com.google.protobuf.util.JsonFormat
import home.kali.report.grpc.*
import home.kali.report.service.ReportService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import com.google.protobuf.Message
import com.google.protobuf.util.JsonFormat.TypeRegistry
import java.util.regex.Pattern

@RestController
@RequestMapping("/grpc")
class GrpcHttpProxyController {

    @Autowired
    private lateinit var reportService: ReportService
    
    private val jsonPrinter = JsonFormat.printer()
        .omittingInsignificantWhitespace()
        .includingDefaultValueFields()
    
    private fun convertJsonForPact(json: String): String {
        // Convert protobuf field names to camelCase for Pact compatibility
        var result = json
        result = result.replace("\"report_id\":".toRegex(), "\"reportId\":")
        result = result.replace("\"estimated_completion_time\":".toRegex(), "\"estimatedCompletionTime\":")
        result = result.replace("\"current_step\":".toRegex(), "\"currentStep\":")
        result = result.replace("\"estimated_time_remaining_seconds\":".toRegex(), "\"estimatedTimeRemainingSeconds\":")
        result = result.replace("\"error_message\":".toRegex(), "\"errorMessage\":")
        result = result.replace("\"file_size_bytes\":".toRegex(), "\"fileSizeBytes\":")
        result = result.replace("\"row_count\":".toRegex(), "\"rowCount\":")
        result = result.replace("\"file_content\":".toRegex(), "\"fileContent\":")
        result = result.replace("\"file_name\":".toRegex(), "\"fileName\":")
        result = result.replace("\"content_type\":".toRegex(), "\"contentType\":")
        
        // Convert string numbers to actual numbers for Pact compatibility
        result = result.replace("\"fileSizeBytes\":\"(\\d+)\"".toRegex(), "\"fileSizeBytes\":$1")
        result = result.replace("\"estimatedTimeRemainingSeconds\":\"(\\d+)\"".toRegex(), "\"estimatedTimeRemainingSeconds\":$1")
        result = result.replace("\"rowCount\":\"(\\d+)\"".toRegex(), "\"rowCount\":$1")
        return result
    }

    @PostMapping(
        "/home.kali.report.ReportGeneratorService/GenerateReport",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun generateReport(@RequestBody requestJson: String): String {
        val requestBuilder = GenerateReportRequest.newBuilder()
        JsonFormat.parser().merge(requestJson, requestBuilder)
        val request = requestBuilder.build()
        
        val response = reportService.generateReport(request)
        
        return convertJsonForPact(jsonPrinter.print(response))
    }

    @PostMapping(
        "/home.kali.report.ReportGeneratorService/GetReportStatus",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getReportStatus(@RequestBody requestJson: String): String {
        val requestBuilder = GetReportStatusRequest.newBuilder()
        JsonFormat.parser().merge(requestJson, requestBuilder)
        val request = requestBuilder.build()
        
        val response = reportService.getReportStatus(request)
        
        return convertJsonForPact(jsonPrinter.print(response))
    }

    @PostMapping(
        "/home.kali.report.ReportGeneratorService/CancelReport",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun cancelReport(@RequestBody requestJson: String): String {
        val requestBuilder = CancelReportRequest.newBuilder()
        JsonFormat.parser().merge(requestJson, requestBuilder)
        val request = requestBuilder.build()
        
        val response = reportService.cancelReport(request)
        
        return convertJsonForPact(jsonPrinter.print(response))
    }

    @PostMapping(
        "/home.kali.report.ReportGeneratorService/DownloadReport",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun downloadReport(@RequestBody requestJson: String): String {
        val requestBuilder = DownloadReportRequest.newBuilder()
        JsonFormat.parser().merge(requestJson, requestBuilder)
        val request = requestBuilder.build()
        
        val response = reportService.downloadReport(request)
        
        return convertJsonForPact(jsonPrinter.print(response))
    }
}
