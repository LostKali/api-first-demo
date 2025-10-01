package home.kali.report.grpc

import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.State
import au.com.dius.pact.provider.junitsupport.VerificationReports
import au.com.dius.pact.provider.junitsupport.loader.PactBroker
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth
import au.com.dius.pact.provider.junit5.HttpTestTarget
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean

@Provider("ReportGeneratorService")
@PactBroker(
    host = "localhost",
    scheme = "http",
    port = "9292",
    authentication = PactBrokerAuth(username = "pact", password = "password")
)
@VerificationReports
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ["grpc.server.port=9090", "spring.profiles.active=test"])
class ReportGeneratorProviderTests {

    @LocalServerPort
    protected var port: Int = 0

    // @MockitoBean
    // private lateinit var reportGeneratorServiceImpl: ReportGeneratorServiceImpl

    @BeforeEach
    fun setUp(context: PactVerificationContext) {
        context.target = HttpTestTarget(port = port)
        System.setProperty("pact.verifier.publishResults", "true")
        System.setProperty("pact.provider.version", "0.0.1-SNAPSHOT")
        System.setProperty("pact.provider.branch", "main")
        System.setProperty("pact.provider.tag", "latest")
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider::class)
    fun testTemplate(context: PactVerificationContext) {
        context.verifyInteraction()
    }

    @State("report_generation_requested")
    fun generateReport() {
        // State setup for report generation
        // The actual service implementation will handle the request
    }

    @State("report_exists")
    fun getReportStatus() {
        // State setup for report status check
    }

    @State("report_in_progress")
    fun cancelReport() {
        // State setup for report cancellation
    }

    @State("report_completed")
    fun downloadReport() {
        // State setup for report download
    }
}
