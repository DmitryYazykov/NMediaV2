package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Long = 999,
    var likedByMe: Boolean = false,
    var share: Long = 990,
    var shareByMe: Boolean = false,
    val view: Long = 1_193
)