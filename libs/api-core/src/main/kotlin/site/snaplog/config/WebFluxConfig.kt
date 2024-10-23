package site.snaplog.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.web.reactive.accept.RequestedContentTypeResolver
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer
import site.snaplog.response.GlobalResponseWrapper
import site.snaplog.security.resolver.LoginMemberArgumentResolver

@Configuration
class WebFluxConfig(
    private val loginMemberArgumentResolver: LoginMemberArgumentResolver
): WebFluxConfigurer {

    @Bean
    fun requestWrapper(
        serverCodecConfigurer: ServerCodecConfigurer,
        requestedContentTypeResolver: RequestedContentTypeResolver
    ): GlobalResponseWrapper {
        return GlobalResponseWrapper(serverCodecConfigurer.writers, requestedContentTypeResolver)
    }

    override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {
        configurer.addCustomResolver(loginMemberArgumentResolver)
    }
}