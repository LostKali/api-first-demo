package home.kali.report.pact

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.slf4j.LoggerFactory

@RestController
@RequestMapping("/pact")
class PactStateChangeController {

    private val logger = LoggerFactory.getLogger(PactStateChangeController::class.java)
    @PostMapping("/stateChange")
    fun handle(@RequestBody map: Map<String, String>) {
        logger.info("Pact state change request: {}", map)
    }
}
