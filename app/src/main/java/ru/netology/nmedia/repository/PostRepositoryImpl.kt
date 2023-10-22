package ru.netology.nmedia.repository

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okio.IOException
import ru.netology.nmedia.api.*
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.toDto
import ru.netology.nmedia.entity.toEntity
import ru.netology.nmedia.errors.ErrorsApplication

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {
    override val data = dao.getAll()
        .map(List<PostEntity>::toDto)
        .flowOn(Dispatchers.Default)

    override fun getNeverCount(id: Long): Flow<Int> = flow {
        while (true) {
            delay(10_000L)
            val response = PostsApi.service.getNewer(id)
            if (!response.isSuccessful) {
                throw ErrorsApplication.ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ErrorsApplication.ApiError(
                response.code(),
                response.message()
            )
            dao.insert(body.toEntity())
            emit(body.size)
        }
    }
        .catch { e-> throw ErrorsApplication.AppError.from(e) }
        .flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        try {
            val response = PostsApi.service.getAll()
            if (!response.isSuccessful) {
                throw ErrorsApplication.ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ErrorsApplication.ApiError(
                response.code(),
                response.message()
            )
            dao.insert(body.toEntity())
        } catch (e: IOException) {
            throw ErrorsApplication.NetworkError
        } catch (e: Exception) {
            throw ErrorsApplication.UnknownError
        }
    }

    override suspend fun save(post: Post) {
        val response = try {
            PostsApi.service.save(post)
        } catch (e: IOException) {
            throw ErrorsApplication.NetworkError
        } catch (e: Exception) {
            throw ErrorsApplication.UnknownError
        }
        if (!response.isSuccessful) {
            throw ErrorsApplication.ApiError(response.code(), response.message())
        }

        val body = response.body() ?: throw ErrorsApplication.ApiError(
            response.code(),
            response.message()
        )
        dao.insert(PostEntity.fromDto(body))
    }

    override suspend fun removePost(id: Long) {
        dao.removeById(id)
        try {
            val response = PostsApi.service.removeById(id)
            if (!response.isSuccessful) {
                throw ErrorsApplication.ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw ErrorsApplication.NetworkError
        } catch (e: Exception) {
            throw ErrorsApplication.UnknownError
        }
    }

    override suspend fun likePost(post: Post) {
        dao.likeById(post.id)
        try {
            val response = if (!post.likedByMe) {
                PostsApi.service.likeById(post.id)
            } else {
                PostsApi.service.dislikeById(post.id)
            }
            if (!response.isSuccessful) {
                throw ErrorsApplication.ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw ErrorsApplication.NetworkError
        } catch (e: Exception) {
            throw ErrorsApplication.UnknownError
        }
    }
}