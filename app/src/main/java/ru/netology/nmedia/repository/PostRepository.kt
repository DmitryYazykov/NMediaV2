package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    val data: Flow<List<Post>>
    fun getNeverCount(id: Long): Flow<Int>
    suspend fun getAll()
    suspend fun save(post: Post)
    suspend fun removePost(id: Long)
    suspend fun likePost(post: Post)
}