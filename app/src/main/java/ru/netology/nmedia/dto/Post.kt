package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String = "",
    val authorAvatar: String = "",
    val content: String = "",
    val published: String = "",
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val sharedByMe: Boolean = false,
    val shares: Int = 0,
    val views: Int = 0,
)