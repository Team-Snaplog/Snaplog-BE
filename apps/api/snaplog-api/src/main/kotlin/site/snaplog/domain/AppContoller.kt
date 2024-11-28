package site.snaplog.domain

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class AppContoller {

    @Value("\${EXECUTION_ENV}")
    lateinit var executionEnv: String

    @GetMapping("/_health")
    fun healthCheck(): Mono<String> {
        return Mono.just("Snaplog API is running on $executionEnv")
    }
}