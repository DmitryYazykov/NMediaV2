package ru.netology.nmedia.errors

import okio.IOException
import java.lang.RuntimeException
import java.sql.SQLException

abstract class ErrorsApplication(var text: String) : RuntimeException() {
    sealed class AppError(var code: String) : RuntimeException() {
        companion object {
            fun from(e: Throwable): AppError = when (e) {
                is AppError -> e
                is SQLException -> DbError
                is IOException -> NetworkError
                else -> UnknownError
            }
        }
    }
    class ApiError(val status: Int, code: String) : AppError(code)
    object NetworkError : AppError("error_network")
    object DbError : AppError("error_db")
    object UnknownError : AppError("error_unknown")
}