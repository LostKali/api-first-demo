package home.kali.admin.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin")
class AdminController {

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello World from Admin Service!"
    }
} 