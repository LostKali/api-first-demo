package home.kali.report.sdk.configuration

import home.kali.report.sdk.client.InternalReportGeneratorFeignClient
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("feign")
@Configuration
@ImportAutoConfiguration(FeignAutoConfiguration::class)
@EnableFeignClients(basePackageClasses = [InternalReportGeneratorFeignClient::class])
class InternalReportGeneratorFeignClientConfiguration