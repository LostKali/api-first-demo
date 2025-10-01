package home.kali.report.sdk.configuration

import home.kali.report.sdk.client.InternalHttpExchangeReportGeneratorApi
import home.kali.report.sdk.client.InternalReportGeneratorFeignClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Profile("http-exchange")
@Configuration
class InternalReportGeneratorHttpExchangeClientConfiguration {
    @Bean
    @ConditionalOnMissingBean(InternalReportGeneratorFeignClient::class)
    fun reportGeneratorClient(@Value("\${report-generator.url}") url: String): InternalHttpExchangeReportGeneratorApi {
        val client = RestClient.builder().baseUrl(url).build()
        return HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(client)).build()
            .createClient(InternalHttpExchangeReportGeneratorApi::class.java)
    }
}