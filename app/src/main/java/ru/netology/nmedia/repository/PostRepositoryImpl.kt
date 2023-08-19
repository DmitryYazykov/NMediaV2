package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit

class PostRepositoryImpl(
    private val dao: PostDao,
) : PostRepository {

    // Настраиваю okhttp клиент
    private val client = OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
        .build()

    // получаемый тип
    private val typeToken = object : TypeToken<List<Post>>() {}.type

    // парсинг ответов
    private val gson = Gson()

    // константы
    private companion object {
        const val BASE_URL = "http://10.0.2.2:9999/"
        val mediaType = "application/json".toMediaType()
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
            .let { it.body?.string() ?: error("Body is null") }
            // преобразование строки в нужный вид
            .let { gson.fromJson(it, typeToken) }
    }

//    override fun getAll(): LiveData<List<Post>> = dao.getAll().map { list ->
//        list.map {
//            it.toDto()
//        }
//    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun save(post: Post): Post {
        val request = Request.Builder()               // запрос на сохранение поста
            .url("${BASE_URL}api/slow/posts")
            .post(gson.toJson(post).toRequestBody(mediaType))
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