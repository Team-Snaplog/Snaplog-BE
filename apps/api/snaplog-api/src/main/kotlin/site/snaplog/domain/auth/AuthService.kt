package site.snaplog.domain.auth

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import site.snaplog.adaptor.MemberAdaptor
import site.snaplog.domain.auth.dto.request.LoginRequestDto
import site.snaplog.domain.auth.dto.request.RefreshRequestDto
import site.snaplog.domain.auth.dto.response.LoginResponseDto
import site.snaplog.domain.auth.validator.AppleOAuth2Validator
import site.snaplog.domain.auth.validator.GoogleOAuth2Validator
import site.snaplog.entity.MemberEntity
import site.snaplog.enums.Provider
import site.snaplog.enums.StatusCode
import site.snaplog.exception.SnaplogException
import site.snaplog.security.service.JwtService

@Service
class AuthService(
    private val jwtService: JwtService,
    private val memberAdaptor: MemberAdaptor,
    private val googleOAuth2Validator: GoogleOAuth2Validator,
    private val appleOAuth2Validator: AppleOAuth2Validator
) {

    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    fun login(loginRequestDto: LoginRequestDto): Mono<LoginResponseDto> {
        logger.debug("로그인 요청")
        return getEmailFromProvider(loginRequestDto)
            .flatMap { email ->
                logger.debug("idToken 검증 완료, email: $email")
                memberAdaptor.findMemberByEmail(email)
                    .switchIfEmpty(Mono.error(SnaplogException(StatusCode.UNAUTHORIZED, "가입되지 않은 회원입니다.")))
            }
            .flatMap { memberEntity ->
                logger.debug("회원 조회 완료, email: ${memberEntity.email}")
                jwtService.deleteBlacklist(memberEntity.email)
                    .thenReturn(memberEntity)
            }
            .flatMap { memberEntity ->
                logger.debug("블랙리스트 삭제 완료, email: ${memberEntity.email}")
                jwtService.issueTokens(memberEntity.email)
            }
            .map { jwtPair ->
                logger.debug("토큰 발급 완료")
                LoginResponseDto(
                    accessToken = jwtPair.first,
                    refreshToken = jwtPair.second
                )
            }
    }

    private fun getEmailFromProvider(loginRequestDto: LoginRequestDto): Mono<String> {
        return when (loginRequestDto.provider) {
            Provider.GOOGLE -> googleOAuth2Validator.validate(loginRequestDto.idToken)
            Provider.APPLE -> appleOAuth2Validator.validate(loginRequestDto.idToken)
        }
    }

    fun logout(loginMember: MemberEntity, accessToken: String): Mono<Void> {
        return jwtService.getJwtPayload(accessToken)
            .flatMap { jwtPayload ->
                if (loginMember.email != jwtPayload["sub"]) {
                    Mono.error(SnaplogException(StatusCode.UNAUTHORIZED, "토큰의 소유자가 아닙니다."))
                } else {
                    Mono.just(jwtPayload)
                }
            }
            .flatMap { jwtPayload ->
                jwtService.deleteJwtCache(loginMember.email)
                    .doOnSuccess { logger.debug("해당 회원 JWT 캐시 정보 삭제 완료") }
                    .thenReturn(jwtPayload["exp"] as Long - System.currentTimeMillis())
            }
            .flatMap { durationMillis ->
                jwtService.saveBlacklist(loginMember.email, durationMillis)
                    .doOnSuccess { logger.debug("블랙리스트 저장 완료") }
            }
            .then()
    }

    fun refresh(loginMember: MemberEntity, refreshRequestDto: RefreshRequestDto): Mono<LoginResponseDto> {
        return jwtService.getJwtPayload(refreshRequestDto.refreshToken)
            .flatMap { jwtPayload ->
                if (loginMember.email != jwtPayload["sub"]) {
                    Mono.error(SnaplogException(StatusCode.UNAUTHORIZED, "토큰의 소유자가 아닙니다."))
                } else {
                    Mono.just(jwtPayload)
                }
            }
            .flatMap {
                jwtService.findJwtCacheByEmail(loginMember.email)
                    .switchIfEmpty(Mono.error(SnaplogException(StatusCode.UNAUTHORIZED, "JWT 캐시 정보가 존재하지 않는 회원입니다.")))
            }
            .flatMap { jwtCache ->
                if (jwtCache.refreshToken != refreshRequestDto.refreshToken) {
                    Mono.error(SnaplogException(StatusCode.UNAUTHORIZED, "유효하지 않은 RefreshToken입니다."))
                } else {
                    Mono.just(jwtCache)
                }
            }
            .flatMap { jwtCache ->
                logger.debug("RefreshToken 유효성 검증 완료")
                jwtService.issueTokens(jwtCache.email)
            }
            .map { jwtPair ->
                logger.debug("토큰 재발급 완료")
                LoginResponseDto(
                    accessToken = jwtPair.first,
                    refreshToken = jwtPair.second
                )
            }
    }
}