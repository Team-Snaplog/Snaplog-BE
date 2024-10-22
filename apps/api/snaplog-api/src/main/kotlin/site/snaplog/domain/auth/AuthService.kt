package site.snaplog.domain.auth

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import site.snaplog.domain.auth.dto.request.LoginRequestDto
import site.snaplog.domain.auth.dto.response.LoginResponseDto
import site.snaplog.enums.Provider
import site.snaplog.enums.StatusCode
import site.snaplog.exception.SnaplogException
import site.snaplog.repository.MemberRepository
import site.snaplog.security.service.JwtService

@Service
class AuthService(
    private val jwtService: JwtService,
    private val memberRepository: MemberRepository
) {

    @Value("\${oauth2.ios.google.client-id}")
    private lateinit var googleClientId: String

    private val googleVerifier by lazy {
        GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory())
            .setAudience(listOf(googleClientId))
            .build()
    }


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
            Provider.GOOGLE -> verifyGoogleToken(loginRequestDto.idToken)
            Provider.APPLE -> verifyAppleToken(loginRequestDto.idToken)
        }
    }

    private fun verifyGoogleToken(idToken: String): Mono<String> {
        return Mono.fromCallable {
            val verifiedIdToken = googleVerifier.verify(idToken)
                ?: throw SnaplogException(StatusCode.UNAUTHORIZED, "Google IdToken이 유효하지 않아 회원 정보를 가져올 수 없습니다.")
            verifiedIdToken.payload.email ?: throw SnaplogException(StatusCode.UNAUTHORIZED, "Google IdToken에서 이메일 정보를 가져올 수 없습니다.")
        }.subscribeOn(Schedulers.boundedElastic())
    }

    private fun verifyAppleToken(idToken: String): Mono<String> {
        return Mono.error(SnaplogException(StatusCode.BAD_REQUEST, "아직 지원하지 않는 로그인 방식입니다."))
    }
}