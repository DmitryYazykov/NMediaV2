package ru.netology.nmedia.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dto.Post
import kotlin.RuntimeException

class PostRepositoryImpl : PostRepository {
    override fun getAll(): List<Post> {
        return PostsApi.service.getAll()
            .execute()
            .let { it.body() ?: throw RuntimeException("Body is null") }
    }

    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        PostsApi.service.getAll()
            .enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    if (response.code() != null) {
                        callback.onError(response.code())
                    } else {
                        callback.onError(RuntimeException(response.errorBody()?.string()))
                    }
                    return
                }

                val body = (response.body() ?: run {
                    callback.onError(RuntimeException("response is empty"))

                    return
                })

                callback.onSuccess(body)
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }
        })
    }

    override fun saveAsync(post: Post, callback: PostRepository.Callback<Unit>) {
        PostsApi.service.savePost(post)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.errorBody()?.string()))
                        return
                    }
                    callback.onSuccess(Unit)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }
            })
    }

    override fun likeByIdAsync(id: Long, callback: PostRepository.Callback<Unit>) {
        PostsApi.service.likePost(id)
            .enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError(RuntimeException(response.errorBody()?.string()))
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }
        })
    }

    override fun unlikeByIdAsync(id: Long, callback: PostRepository.Callback<Unit>) {
        PostsApi.service.unlikePost(id)
            .enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError(RuntimeException(response.errorBody()?.string()))
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }
        })
    }

    override fun removeAsync(id: Long, callback: PostRepository.Callback<Unit>) {
        PostsApi.service.deletePost(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.errorBody()?.string()))
                    return
                }
                callback.onSuccess(Unit)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }
        })
    }
}