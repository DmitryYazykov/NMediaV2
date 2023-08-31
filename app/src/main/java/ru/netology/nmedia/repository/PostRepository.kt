package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {

    // синхронный
    fun likeById(id: Long): Post
    fun unlikeById(id: Long): Post
    fun save(post: Post): Post
    fun removeById(id: Long)

    // асинхронный
    fun getAllAsync(callback: Callback<List<Post>>)
    fun saveAsync(post: Post, callback: Callback<Unit>)

    interface Callback<T> {                            // общий дженерик-интерфейс
        fun onSuccess(posts: T)
        fun onError()
    }
}