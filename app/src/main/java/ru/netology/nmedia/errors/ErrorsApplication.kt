package ru.netology.nmedia.errors

import java.lang.RuntimeException

abstract class ErrorsApplication(text: String) : RuntimeException() {
    //class ErrApi(val status: Int, code: String): ErrorsApplication(code)
    data object AppError : ErrorsApplication("Error Application")
    data object NetWorkError : ErrorsApplication("Network Error")
    data object UnknownError : ErrorsApplication("Unknown Error")
    data object BodyError : ErrorsApplication("body is null")
}