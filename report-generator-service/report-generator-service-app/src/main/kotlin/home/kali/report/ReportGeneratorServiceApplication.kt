package home.kali.report

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReportGeneratorServiceApplication

fun main(args: Array<String>) {
    runApplication<ReportGeneratorServiceApplication>(*args)
} 