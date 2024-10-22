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
            .bodyToMono(Map::class.java)
            .map { it["keys"] as List<*> }
            .map { it.map { key -> key as Map<*, *> } }
    }

    private fun validateToken(idToken: String, publicKeys: List<Map<*, *>>): Mono<String> {
        return Mono.fromCallable {
            val signedJWT = SignedJWT.parse(idToken)
            val kid = signedJWT.header.keyID

            val publicKey = findMatchingPublicKey(publicKeys, kid)
            validateSignature(signedJWT, publicKey)

            extractEmail(signedJWT)
        }
    }

    private fun findMatchingPublicKey(publicKeys: List<Map<*, *>>, kid: String): RSAKey {
        val keyData = publicKeys.find { it["kid"] == kid }
            ?: throw SnaplogException(StatusCode.UNAUTHORIZED, "Apple IdToken이 유효하지 않아 회원 정보를 가져올 수 없습니다.")

        return RSAKey.Builder(
            Base64URL(keyData["n"] as String),
            Base64URL(keyData["e"] as String)
        ).build()
    }

    private fun validateSignature(signedJWT: SignedJWT, rsaKey: RSAKey) {
        val verifier = RSASSAVerifier(rsaKey)
        if (!signedJWT.verify(verifier)) {
            throw SnaplogException(
                StatusCode.UNAUTHORIZED,
                "Apple IdToken이 유효하지 않아 회원 정보를 가져올 수 없습니다."
            )
        }
    }

    private fun extractEmail(signedJWT: SignedJWT): String {
        return signedJWT.jwtClaimsSet.getStringClaim("email")
            ?: throw SnaplogException(
                StatusCode.UNAUTHORIZED,
                "Apple IdToken에서 이메일을 찾을 수 없습니다."
            )
    }
}