package home.kali.admin

import au.com.dius.pact.provider.junit5.HttpTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.State
import au.com.dius.pact.provider.junitsupport.VerificationReports
import au.com.dius.pact.provider.junitsupport.loader.PactBroker
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles

/**
 * Pact Provider Test for Admin Service
 * Verifies that Admin Service meets the contracts published by consumers (Report UI)
 */
@Provider("AdminService")
@PactBroker(
    host = "localhost",
    scheme = "http",
    port = "9292",
    authentication = PactBrokerAuth(username = "pact", password = "password")
)
@VerificationReports
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AdminServicePactProviderTest {

    @LocalServerPort
    protected var port: Int = 0

    @BeforeEach
    fun setUp(context: PactVerificationContext) {
        context.target = HttpTestTarget(port = port)
        System.setProperty("pact.verifier.publishResults", "true")
        System.setProperty("pact.provider.version", "0.0.1-SNAPSHOT")
        System.setProperty("pact.provider.branch", "main")
        System.setProperty("pact.provider.tag", "latest")
        System.setProperty("pact.verifier.enablePending", "true")
        System.setProperty("pact.verifier.includeWipPactsSince", "2020-01-01")
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider::class)
    fun testTemplate(context: PactVerificationContext) {
        context.verifyInteraction()
    }

    @State("a report request can be created")
    fun reportRequestCanBeCreated() {
        // Setup state for report creation
        // This method is called before each interaction that has this state
    }

    @State("reports exist")
    fun reportsExist() {
        // Setup state for reports list
        // This method is called before each interaction that has this state
    }

    @State("a report with id 123e4567-e89b-12d3-a456-426614174000 exists")
    fun reportWithIdExists() {
        // Setup state for single report
        // This method is called before each interaction that has this state
    }

    @State("a report with id 123e4567-e89b-12d3-a456-426614174000 can be cancelled")
    fun reportWithIdCanBeCancelled() {
        // Setup state for report cancellation
        // This method is called before each interaction that has this state
    }

    @State("a report with id 123e4567-e89b-12d3-a456-426614174000 is ready for download")
    fun reportWithIdIsReadyForDownload() {
        // Setup state for report download
        // This method is called before each interaction that has this state
    }
}
