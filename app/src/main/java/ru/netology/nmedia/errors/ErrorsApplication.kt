package ru.netology.nmedia.errors

import java.lang.RuntimeException
abstract class ErrorsApplication(var text: String) : RuntimeException() {
    class ApiError(val status: Int, code: String): ErrorsApplication(code)
    data object NetworkError : ErrorsApplication("error_network")
    data object UnknownError: ErrorsApplication("error_unknown")

}