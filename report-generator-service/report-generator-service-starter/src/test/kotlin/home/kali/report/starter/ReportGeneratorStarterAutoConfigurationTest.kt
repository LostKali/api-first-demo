package home.kali.report.starter

import home.kali.report.api.InternalReportGeneratorApi
import home.kali.report.sdk.client.InternalReportGeneratorFeignClient
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest(classes = [ReportGeneratorStarterAutoConfiguration::class])
@TestPropertySource(properties = ["report-generator.url=http://localhost:8080"])
@ActiveProfiles("http-exchange")
class HttpExchangeStarterAutoConfigurationTest {

    @Autowired(required = false)
    private var httpExchangeClient: InternalReportGeneratorApi? = null

    @Autowired(required = false)
    private var feignClient: InternalReportGeneratorFeignClient? = null

    @Test
    fun `should auto-configure HttpExchange client when http-exchange profile is active`() {
        assertNotNull(httpExchangeClient, "HttpExchange client should be auto-configured")
        assertNull(feignClient, "Feign client should not be auto-configured")
    }
}

@SpringBootTest(classes = [ReportGeneratorStarterAutoConfiguration::class])
@TestPropertySource(properties = ["report-generator.url=http://localhost:8080"])
@ActiveProfiles("feign")
class FeignStarterAutoConfigurationTest {

    @Autowired(required = false)
    private var httpExchangeClient: InternalReportGeneratorApi? = null

    @Autowired(required = false)
    private var feignClient: InternalReportGeneratorFeignClient? = null

    @Test
    fun `should auto-configure Feign client when feign profile is active`() {
        assertNull(httpExchangeClient, "HttpExchange client should not be auto-configured")
        assertNotNull(feignClient, "Feign client should be auto-configured")
    }
}

@SpringBootTest(classes = [ReportGeneratorStarterAutoConfiguration::class])
@TestPropertySource(properties = ["report-generator.url=http://localhost:8080"])
@ActiveProfiles("default")
class DefaultStarterAutoConfigurationTest {

    @Autowired(required = false)
    private var httpExchangeClient: InternalReportGeneratorApi? = null

    @Autowired(required = false)
    private var feignClient: InternalReportGeneratorFeignClient? = null

    @Test
    fun `should not auto-configure any client when no specific profile is active`() {
        assertNull(httpExchangeClient, "HttpExchange client should not be auto-configured")
        assertNull(feignClient, "Feign client should not be auto-configured")
    }
}
