package site.snaplog.domain.auth

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import site.snaplog.domain.auth.dto.request.LoginRequestDto
import site.snaplog.domain.auth.dto.response.LoginResponseDto
import site.snaplog.security.service.JwtService

@Service
class AuthService(
    private val jwtService: JwtService
) {

    fun login(loginRequestDto: LoginRequestDto): Mono<LoginResponseDto> {
        return Mono.just(LoginResponseDto(
            accessToken = "accessToken",
            refreshToken = "refreshToken"
        ))
    }
}