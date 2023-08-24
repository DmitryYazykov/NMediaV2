package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

class PostRepositoryImpl(
    private val dao: PostDao,
) : PostRepository {

    // Настраиваю okhttp клиент
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    // парсинг ответов
    private val gson = Gson()

    // получаемый тип
    private val typeToken = object : TypeToken<List<Post>>() {}.type


    // константы
    private companion object {
        const val BASE_URL = "http://10.0.2.2:9999/"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAll(): List<Post> {
        // подготовим запрос
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()
        // использование клиента для создания вызова - возвращаем результат
        return client.newCall(request)
            .execute()
            // прочитать ответ в виде строки либо ошибка
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            // преобразование строки в нужный вид
            .let { gson.fromJson(it, typeToken) }
    }

//    override fun getAll(): LiveData<List<Post>> = dao.getAll().map { list ->
//        list.map {
//            it.toDto()
//        }
//    }

    override fun likeById(id: Long): Post {                     // запрос на постановку лайка
        //dao.likeById(id)
        val request = Request.Builder()
            .post(EMPTY_REQUEST)
            .url("${BASE_URL}api/posts/$id/likes")
            .build()

        return client.newCall(request)
            .execute()
            .body?.string()
            ?.let { gson.fromJson(it, Post::class.java) }
            ?: throw RuntimeException("body is null")
    }

    override fun unlikeById(id: Long): Post {                  // запрос на удаление лайка
        val request = Request.Builder()
            .delete(EMPTY_REQUEST)
            .url("${BASE_URL}api/posts/$id/likes")
            .build()

        return client.newCall(request)
            .execute()
            .body?.string()
            ?.let { gson.fromJson(it, Post::class.java) }
            ?: throw RuntimeException("body is null")
    }

    override fun save(post: Post): Post {
        val request = Request.Builder()               // запрос на сохранение поста
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}api/slow/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let {
                it.body?.string() ?: error("Body is null")
            }.let {
                gson.fromJson(it, post::class.java)
            }
    }

    override fun removeById(id: Long) {
        val request = Request.Builder()              // запрос на удаление поста
            .url("${BASE_URL}api/slow/posts/$id")
            .delete()
            .build()
        client.newCall(request)
            .execute()
    }
}