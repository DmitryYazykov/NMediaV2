package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val attachment: Attachment? = null
) {
    val shares: Int =0
}
data class Attachment(
    val url: String = "",
    val type: String = ""
){
    val description : String = ""
}