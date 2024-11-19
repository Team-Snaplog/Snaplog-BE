package site.snaplog.domain.auth

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import site.snaplog.domain.auth.dto.request.LoginRequestDto
import site.snaplog.domain.auth.dto.request.RefreshRequestDto
import site.snaplog.domain.auth.dto.response.LoginResponseDto
import site.snaplog.entity.MemberEntity
import site.snaplog.security.resolver.AccessToken
import site.snaplog.security.resolver.LoginMember
import site.snaplog.util.consts.Uri

@RestController
@RequestMapping(Uri.AUTH)
@Tag(name = "Auth", description = "인증 관련 API")
class AuthController(
    private val authService: AuthService
) {

    @GetMapping(Uri.VERIFY_TOKEN)
    @Operation(
        summary = "토큰 검증 Api",
        description = "AccessToken이 유효한지 검증하고, 유효하다면 Member 정보를 반환합니다."
    )
    fun verifyToken(@LoginMember loginMember: MemberEntity): Mono<MemberEntity> {
        return Mono.just(loginMember)
    }

    @PostMapping(Uri.LOGIN)
    @Operation(
        summary = "로그인 Api",
        description = "Google 및 Apple 로그인을 통해 발급 받은 Id Token으로 로그인을 진행하고, AccessToken과 RefreshToken을 발급 받습니다."
    )
    fun login(@RequestBody @Valid loginRequestDto: LoginRequestDto): Mono<LoginResponseDto> {
        return authService.login(loginRequestDto)
    }

    @PostMapping(Uri.LOGOUT)
    @Operation(
        summary = "로그아웃 Api",
        description = "AccessToken을 블랙리스트에 추가하여 로그아웃을 진행합니다."
    )
    fun logout(@LoginMember loginMember: MemberEntity, @AccessToken accessToken: String): Mono<Boolean> {
        return authService.logout(loginMember, accessToken)
    }

    @PostMapping(Uri.REFRESH)
    @Operation(
        summary = "토큰 재발급 Api",
        description = "RefreshToken을 통해 유효성을 검증한 후 AccessToken과 RefreshToken을 재발급 받습니다.",
    )
    fun refresh(@LoginMember loginMember: MemberEntity, @RequestBody @Valid refreshRequestDto: RefreshRequestDto): Mono<LoginResponseDto> {
        return authService.refresh(loginMember, refreshRequestDto)
    }
}