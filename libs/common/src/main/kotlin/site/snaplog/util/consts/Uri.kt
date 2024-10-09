package site.snaplog.util.consts

object Uri {

    const val AUTH = "/auth"
    const val LOGIN = "/login"
    const val REFRESH = "/refresh"

    val passUris = listOf(
        AUTH + LOGIN,
        AUTH + REFRESH
    )
}