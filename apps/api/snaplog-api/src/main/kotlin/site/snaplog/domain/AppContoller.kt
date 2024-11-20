package site.snaplog.domain

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AppContoller {

    @Value("\${EXECUTION_ENV}")
    lateinit var executionEnv: String

    @GetMapping("/_health")
    fun healthCheck(): String {
        return "Snaplog API is running on $executionEnv"
    }
}