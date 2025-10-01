package home.kali.report.sdk.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    InternalReportGeneratorFeignClientConfiguration::class,
    InternalReportGeneratorHttpExchangeClientConfiguration::class
)
class InternalReportGeneratorClientSdkConfiguration