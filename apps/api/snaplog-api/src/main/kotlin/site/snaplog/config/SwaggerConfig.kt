package site.snaplog.config

import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openApiCustomiser(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi ->
            openApi.info(
                Info()
                    .title("Snaplog API")
                    .description(
                        """
                        Snaplog API
                        """.trimIndent()
                    )
                    .version("1.0.0")
            )
        }
    }
}