package home.kali.report.starter

import home.kali.report.sdk.client.InternalReportGeneratorFeignClient
import home.kali.report.sdk.configuration.InternalReportGeneratorClientSdkConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ConditionalOnMissingBean(InternalReportGeneratorFeignClient::class)
@Import(InternalReportGeneratorClientSdkConfiguration::class)
class ReportGeneratorStarterAutoConfiguration {
}
