package site.snaplog.domain.auth.validator

import reactor.core.publisher.Mono

interface OAuth2Validator {
    fun validate(idToken: String): Mono<String>
}