package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {

    // синхронный
    fun getAll(): List<Post>

    // асинхронный
    fun getAllAsync(callback: Callback<List<Post>>)
    fun saveAsync(post: Post, callback: Callback<Unit>)
    fun likeByIdAsync(id: Long, callback: Callback<Unit>)
    fun unlikeByIdAsync(id: Long, callback: Callback<Unit>)

    //fun likeAsync(likedPost: Post, callback: Callback<Post>)
    fun removeAsync(id: Long, callback: Callback<Unit>)

    interface Callback<T> {                            // общий дженерик-интерфейс
        fun onSuccess(result: T)
        fun onError(e: Exception)
    }
}