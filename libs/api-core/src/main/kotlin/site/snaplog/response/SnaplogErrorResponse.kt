package site.snaplog.response

data class SnaplogErrorResponse(
    override val status: Int,
    override val message: String,
    val errorDetail: String? = null,
): SnaplogResponse<Nothing>(status, message)