package site.snaplog.response

open class SnaplogResponse<T>(
    open val status: Int,
    open val message: String,
    val data: T? = null,
)
