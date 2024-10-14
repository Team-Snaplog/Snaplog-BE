package site.snaplog.util.consts

object Uri {
    const val V3 = "/v3"
    const val API_DOCS = "/api-docs"
    const val WEBJARS = "/webjars"

    const val AUTH = "/auth"
    const val LOGIN = "/login"
    const val REFRESH = "/refresh"

    val passUris = listOf(
        API_DOCS,
        WEBJARS + "/**",
        V3 + API_DOCS + "/**",

        AUTH + LOGIN,
        AUTH + REFRESH
    )
}