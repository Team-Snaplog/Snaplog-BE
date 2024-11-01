package site.snaplog.domain.auth.validator

import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.util.Base64URL
import com.nimbusds.jwt.SignedJWT
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import site.snaplog.enums.StatusCode
import site.snaplog.exception.SnaplogException

@Service
class AppleOAuth2Validator(
    private val webClient: WebClient
): OAuth2Validator {

    companion object {
        private const val APPLE_PUBLIC_KEYS_URL = "https://appleid.apple.com/auth/keys"
    }

    override fun validate(idToken: String): Mono<String> {
        return getApplePublicKeys()
            .flatMap { publicKeys ->
                validateToken(idToken, publicKeys)
            }
    }

    private fun getApplePublicKeys(): Mono<List<Map<*, *>>> {
        return webClient.get()
            .uri(APPLE_PUBLIC_KEYS_URL)
            .retrieve()
            .onStatus(
                { status -> status.isError },
                {
                    Mono.error(
                        SnaplogException(
                            StatusCode.UNAUTHORIZED,
                            "Apple 공개키 조회 실패"
                        )
                    )
                }
            )
            .bodyToMono(Map::class.java)
            .map { it["keys"] as List<*> }
            .map { it.map { key -> key as Map<*, *> } }
    }

    private fun validateToken(idToken: String, publicKeys: List<Map<*, *>>): Mono<String> {
        val signedJWT = SignedJWT.parse(idToken)
        val kid = signedJWT.header.keyID
        return findMatchingPublicKey(publicKeys, kid)
            .flatMap { rsaKey ->
                validateSignature(SignedJWT.parse(idToken), rsaKey)
            }
            .flatMap {
                extractEmail(signedJWT)
            }
    }

    private fun findMatchingPublicKey(publicKeys: List<Map<*, *>>, kid: String): Mono<RSAKey> {
        return Mono.justOrEmpty(publicKeys.find { it["kid"] == kid })
            .switchIfEmpty(
                Mono.error(
                    SnaplogException(
                        StatusCode.UNAUTHORIZED,
                        "Apple IdToken이 유효하지 않아 회원 정보를 가져올 수 없습니다."
                    )
                )
            )
            .map { keyData ->
                RSAKey.Builder(
                    Base64URL(keyData["n"] as String),
                    Base64URL(keyData["e"] as String)
                ).build()
            }
    }

    private fun validateSignature(signedJWT: SignedJWT, rsaKey: RSAKey): Mono<Unit> {
        val verifier = RSASSAVerifier(rsaKey)
        return if (!signedJWT.verify(verifier)) {
            Mono.error(
                SnaplogException(
                    StatusCode.UNAUTHORIZED,
                    "Apple IdToken이 유효하지 않아 회원 정보를 가져올 수 없습니다."
                )
            )
        } else {
            Mono.empty()
        }
    }

    private fun extractEmail(signedJWT: SignedJWT): Mono<String> {
        return Mono.justOrEmpty(signedJWT.jwtClaimsSet.getStringClaim("email"))
            .switchIfEmpty(
                Mono.error(
                    SnaplogException(
                        StatusCode.UNAUTHORIZED,
                        "Apple IdToken에서 이메일을 찾을 수 없습니다."
                    )
                )
            )
    }
}