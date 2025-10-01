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
        InternalReportGeneratorClientSdkConfiguration::class
    ]
)
@TestPropertySource(properties = ["report-generator.url=http://localhost:8080"])
@ActiveProfiles("feign")
class FeignProfileApplicationContextTest {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired(required = false)
    private var httpExchangeClient: InternalHttpExchangeReportGeneratorApi? = null

    @Autowired(required = false)
    private var feignClient: InternalReportGeneratorFeignClient? = null

    @Test
    fun `should create Feign client and not create HttpExchange client when feign profile is active`() {
        assertNotNull(feignClient, "Feign client should be created when feign profile is active")

        assertNull(httpExchangeClient, "HttpExchange client should not be created when feign profile is active")

        val httpExchangeBeans = applicationContext.getBeansOfType(InternalHttpExchangeReportGeneratorApi::class.java)
        val feignBeans = applicationContext.getBeansOfType(InternalReportGeneratorFeignClient::class.java)

        assert(feignBeans.isNotEmpty()) { "Should have Feign client beans" }
        assert(httpExchangeBeans.isEmpty()) { "Should not have HttpExchange client beans" }
    }
}