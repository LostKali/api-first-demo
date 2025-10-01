package home.kali.report.starter

import home.kali.report.api.InternalReportGeneratorApi
import home.kali.report.sdk.client.InternalReportGeneratorFeignClient
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest(classes = [ReportGeneratorStarterAutoConfiguration::class])
@TestPropertySource(properties = ["report-generator.url=http://localhost:8080"])
@ActiveProfiles("http-exchange")
class HttpExchangeStarterIntegrationTest {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Test
    fun `should register HttpExchange client bean in application context when http-exchange profile is active`() {
        val httpExchangeClient = applicationContext.getBean(InternalReportGeneratorApi::class.java)
        assertNotNull(httpExchangeClient, "HttpExchange client should be registered in application context")
        
        val beanNames = applicationContext.getBeanNamesForType(InternalReportGeneratorApi::class.java)
        assertTrue(beanNames.isNotEmpty(), "Should have at least one InternalReportGeneratorApi bean")
    }
}

@SpringBootTest(classes = [ReportGeneratorStarterAutoConfiguration::class])
@TestPropertySource(properties = ["report-generator.url=http://localhost:8080"])
@ActiveProfiles("feign")
class FeignStarterIntegrationTest {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Test
    fun `should register Feign client bean in application context when feign profile is active`() {
        val feignClient = applicationContext.getBean(InternalReportGeneratorFeignClient::class.java)
        assertNotNull(feignClient, "Feign client should be registered in application context")
        
        val beanNames = applicationContext.getBeanNamesForType(InternalReportGeneratorFeignClient::class.java)
        assertTrue(beanNames.isNotEmpty(), "Should have at least one InternalReportGeneratorFeignClient bean")
    }
}
