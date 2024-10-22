package site.snaplog.domain.auth

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import site.snaplog.domain.auth.dto.request.LoginRequestDto
import site.snaplog.domain.auth.dto.response.LoginResponseDto
import site.snaplog.domain.auth.validator.AppleOAuth2Validator
import site.snaplog.domain.auth.validator.GoogleOAuth2Validator
import site.snaplog.enums.Provider
import site.snaplog.enums.StatusCode
import site.snaplog.exception.SnaplogException
import site.snaplog.repository.MemberRepository
import site.snaplog.security.service.JwtService

@Service
class AuthService(
    private val jwtService: JwtService,
    private val memberRepository: MemberRepository,
    private val googleOAuth2Validator: GoogleOAuth2Validator,
    private val appleOAuth2Validator: AppleOAuth2Validator
) {

    fun login(loginRequestDto: LoginRequestDto): Mono<LoginResponseDto> {
        return getEmailFromProvider(loginRequestDto)
            .flatMap { email ->
                memberRepository.findByEmail(email)
                    .switchIfEmpty(Mono.error(SnaplogException(StatusCode.UNAUTHORIZED, "가입되지 않은 회원입니다.")))
            }
            .map { memberEntity ->
                jwtService.issueTokens(memberEntity.email)
            }
            .map { jwtCache ->
                LoginResponseDto(
                    accessToken = jwtCache.accessToken,
                    refreshToken = jwtCache.refreshToken
                )
            }
    }

    private fun getEmailFromProvider(loginRequestDto: LoginRequestDto): Mono<String> {
        return when (loginRequestDto.provider) {
            Provider.GOOGLE -> googleOAuth2Validator.validate(loginRequestDto.idToken)
            Provider.APPLE -> appleOAuth2Validator.validate(loginRequestDto.idToken)
        }
    }
}