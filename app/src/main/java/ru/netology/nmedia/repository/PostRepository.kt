package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun get(): LiveData<Post>  // ф-ия возвращает LiveData от поста (обновляется)
    fun like()                 // ф-ия которая проставляет лайк
    fun share()                // ф-ия которая проставляет поделиться
}