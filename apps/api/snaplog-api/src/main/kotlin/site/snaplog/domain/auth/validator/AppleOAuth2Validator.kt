package site.snaplog.domain.auth.validator

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import site.snaplog.enums.StatusCode
import site.snaplog.exception.SnaplogException

@Service
class AppleOAuth2Validator: OAuth2Validator {

    override fun validate(idToken: String): Mono<String> {
        return Mono.error(SnaplogException(StatusCode.BAD_REQUEST, "Apple OAuth2는 아직 지원하지 않습니다."))
    }
}