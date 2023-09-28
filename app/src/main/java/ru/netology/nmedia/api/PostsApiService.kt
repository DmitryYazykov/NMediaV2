package ru.netology.nmedia.api

import com.google.firebase.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit

// Настраиваю okhttp клиент
private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .let {
        if (BuildConfig.DEBUG)
            it.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }) else {
            it
        }
    }
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(ru.netology.nmedia.BuildConfig.BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(client)
    .build()

interface PostsApiService {
    @GET("posts")
    fun getAll(): Call<List<Post>>

    @POST("posts")
    fun savePost(@Body post: Post): Call<Post>

    @POST("posts")
    fun deletePost(@Path("id") id: Long): Call<Unit>

    @POST("posts/{id}/likes")
    fun likePost(@Path("id") id: Long): Call<Post>

    @DELETE("posts/{id}/likes")
    fun unlikePost(@Path("id") id: Long): Call<Post>
}

// Точка входа в интерфейс
object PostsApi {
    val service: PostsApiService by lazy {
        retrofit.create()
    }
}