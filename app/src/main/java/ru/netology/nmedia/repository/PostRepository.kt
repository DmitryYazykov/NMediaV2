package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    //fun getAll(): LiveData<List<Post>>
    fun getAll(): List<Post>
    fun likeById(id: Long): Post
    fun unlikeById(id: Long): Post
    fun save(post: Post): Post
    fun removeById(id: Long)
}