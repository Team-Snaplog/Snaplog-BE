package site.snaplog.domain.auth.validator

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import site.snaplog.enums.StatusCode
import site.snaplog.exception.SnaplogException

@Service
class GoogleOAuth2Validator(
    @Value("\${oauth2.ios.google.client-id}") private val googleClientId: String
): OAuth2Validator {

    private val googleVerifier by lazy {
        GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory())
            .setAudience(listOf(googleClientId))
            .build()
    }

    override fun validate(idToken: String): Mono<String> {
        return Mono.fromCallable {
            val verifiedIdToken = googleVerifier.verify(idToken)
                ?: throw SnaplogException(StatusCode.UNAUTHORIZED, "Google IdToken이 유효하지 않습니다.")
            verifiedIdToken.payload.email
        }.subscribeOn(Schedulers.boundedElastic())
    }
}