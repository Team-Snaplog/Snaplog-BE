package site.snaplog.exception

import site.snaplog.enums.StatusCode

open class SnaplogException(
    val statusCode: StatusCode,
    override val message: String
): RuntimeException()
