package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Long,
    val likedByMe: Boolean = false,
    val share: Long,
    val shareByMe: Boolean = false,
    val view: Long
)