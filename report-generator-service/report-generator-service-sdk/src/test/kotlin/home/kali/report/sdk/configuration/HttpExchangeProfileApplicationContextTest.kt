package home.kali.report.sdk.configuration

import home.kali.report.sdk.client.InternalHttpExchangeReportGeneratorApi
import home.kali.report.sdk.client.InternalReportGeneratorFeignClient
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest(
    classes = [
        InternalReportGeneratorClientSdkConfiguration::class,
    ]
)
@TestPropertySource(properties = ["report-generator.url=http://localhost:8080"])
@ActiveProfiles("http-exchange")
class HttpExchangeProfileApplicationContextTest {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired(required = false)
    private var httpExchangeClient: InternalHttpExchangeReportGeneratorApi? = null

    @Autowired(required = false)
    private var feignClient: InternalReportGeneratorFeignClient? = null

    @Test
    fun `should create HttpExchange client and not create Feign client when http-exchange profile is active`() {
        assertNotNull(httpExchangeClient, "HttpExchange client should be created when http-exchange profile is active")

        assertNull(feignClient, "Feign client should not be created when http-exchange profile is active")

        val httpExchangeBeans = applicationContext.getBeansOfType(InternalHttpExchangeReportGeneratorApi::class.java)
        val feignBeans = applicationContext.getBeansOfType(InternalReportGeneratorFeignClient::class.java)

        assert(httpExchangeBeans.isNotEmpty()) { "Should have HttpExchange client beans" }
        assert(feignBeans.isEmpty()) { "Should not have Feign client beans" }
    }
}