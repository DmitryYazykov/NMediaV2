package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {                // интерфейс, описывает операции для работы с постами
    fun getAll(): LiveData<List<Post>>    // получения списка всех постов
    fun likeById(id: Long)                // добавления/удаления лайка к посту по заданному идентификатору
    fun shareById(id: Long)               // увеличение счетчика репостов поста с заданным идентификатором
    fun removeById(id: Long)              // удаление поста
    fun save(post: Post)                  // сохранение поста
}