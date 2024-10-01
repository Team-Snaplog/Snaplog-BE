package site.snaplog.util.error

import site.snaplog.util.enums.StatusCode

open class SnaplogError(
    val statusCode: StatusCode,
    override val message: String
): RuntimeException()