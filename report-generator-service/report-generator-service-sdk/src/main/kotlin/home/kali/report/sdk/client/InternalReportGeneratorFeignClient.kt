package home.kali.report.sdk.client

import home.kali.report.generated.api.ReportGenerationApi
import home.kali.report.sdk.configuration.InternalReportGeneratorClientConfiguration
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(
    value = "report-generator-client",
    url = "\${report-generator.url}",
    configuration = [InternalReportGeneratorClientConfiguration::class]
)
interface InternalReportGeneratorFeignClient : ReportGenerationApi