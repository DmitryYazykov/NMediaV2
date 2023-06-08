package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

private val empty = Post(                         // data-объект для заполнения
    id = 0,
    author = "",
    content = "",
    published = "",
    likes = 0,
    likedByMe = false,
    share = 0,
    shareByMe = false,
    view = 0
)

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    val edited = MutableLiveData(empty)
    val cancelVisible = MutableLiveData(false)

    fun cancelEdit() {                   // ф-ия для шаблона пустого поста при отмене редактирования
        edited.value = empty
    }

    fun save() {                                  // ф-ия сохранения контента
        edited.value?.let {
            repository.save(it)
        }
        cancelEdit()
        cancelVisible.value = false
    }

    fun edit(post: Post) {                        // ф-ия изменения контента
        edited.value = post
        cancelVisible.value = true
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content != text) {
            edited.value = edited.value?.copy(content = text)
        }
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
}