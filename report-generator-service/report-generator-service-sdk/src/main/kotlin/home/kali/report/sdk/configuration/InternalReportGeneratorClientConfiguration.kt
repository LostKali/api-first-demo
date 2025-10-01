package home.kali.report.sdk.configuration

import feign.okhttp.OkHttpClient
import org.springframework.cloud.openfeign.FeignFormatterRegistrar
import org.springframework.context.annotation.Bean
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar

class InternalReportGeneratorClientConfiguration {
    @Bean
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

    @Bean
    fun localDateFeignFormatterRegistrar() = FeignFormatterRegistrar { formatterRegistry ->
        val registrar = DateTimeFormatterRegistrar()
        registrar.setUseIsoFormat(true)
        registrar.registerFormatters(formatterRegistry)
    }
}