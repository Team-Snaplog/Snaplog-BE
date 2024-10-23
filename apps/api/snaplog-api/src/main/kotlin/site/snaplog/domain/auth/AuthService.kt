package site.snaplog.domain.auth

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import site.snaplog.adapter.MemberAdapter
import site.snaplog.domain.auth.dto.request.LoginRequestDto
import site.snaplog.domain.auth.dto.response.LoginResponseDto
import site.snaplog.domain.auth.validator.AppleOAuth2Validator
import site.snaplog.domain.auth.validator.GoogleOAuth2Validator
import site.snaplog.enums.Provider
import site.snaplog.enums.StatusCode
import site.snaplog.exception.SnaplogException
import site.snaplog.security.service.JwtService

@Service
class AuthService(
    private val jwtService: JwtService,
    private val memberAdapter: MemberAdapter,
    private val googleOAuth2Validator: GoogleOAuth2Validator,
    private val appleOAuth2Validator: AppleOAuth2Validator
) {

    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    fun login(loginRequestDto: LoginRequestDto): Mono<LoginResponseDto> {
        logger.debug("로그인 요청")
        return getEmailFromProvider(loginRequestDto)
            .flatMap { email ->
                logger.debug("idToken 검증 완료, email: $email")
                memberAdapter.findMemberByEmail(email)
                    .switchIfEmpty(Mono.error(SnaplogException(StatusCode.UNAUTHORIZED, "가입되지 않은 회원입니다.")))
            }
            .flatMap { memberEntity ->
                logger.debug("회원 조회 완료, email: ${memberEntity.email}")
                jwtService.issueTokens(memberEntity.email)
            }
            .map { jwtCache ->
                logger.debug("토큰 발급 완료")
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