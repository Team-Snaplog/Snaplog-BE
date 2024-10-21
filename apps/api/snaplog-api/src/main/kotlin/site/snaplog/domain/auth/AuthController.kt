package site.snaplog.domain.auth

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import site.snaplog.domain.auth.dto.request.LoginRequestDto
import site.snaplog.domain.auth.dto.response.LoginResponseDto
import site.snaplog.util.consts.Uri

@RestController
@RequestMapping(Uri.AUTH)
@Tag(name = "Auth", description = "인증 관련 API")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping(Uri.LOGIN)
    @Operation(
        summary = "로그인 Api",
        description = "Google 및 Apple 로그인을 통해 발급 받은 Id Token으로 로그인을 진행하고, AccessToken과 RefreshToken을 발급 받습니다."
    )
    fun login(@RequestBody loginRequestDto: LoginRequestDto): Mono<LoginResponseDto> {
        return authService.login(loginRequestDto)
    }
}