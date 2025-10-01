package home.kali.report.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/report")
class ReportController {

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello World from Report Generator Service!"
    }
} 