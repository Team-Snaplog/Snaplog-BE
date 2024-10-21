package site.snaplog.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.web.reactive.accept.RequestedContentTypeResolver
import site.snaplog.response.GlobalResponseWrapper

@Configuration
class WebFluxConfig {

    @Bean
    fun requestWrapper(
        serverCodecConfigurer: ServerCodecConfigurer,
        requestedContentTypeResolver: RequestedContentTypeResolver
    ): GlobalResponseWrapper {
        return GlobalResponseWrapper(serverCodecConfigurer.writers, requestedContentTypeResolver)
    }
}