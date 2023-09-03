package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.util.SingleLiveEvent
import kotlin.Exception
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
    likedByMe = false,
    likes = 0,
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryImpl(
        //AppDb.getInstance(context = application).postDao()
    )
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel> = _data
    val edited = MutableLiveData(empty)

    // создаём событие - пост был создан
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit> = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.postValue(FeedModel(loading = true))
        repository.getAllAsync(object : PostRepository.Callback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError() {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun save() {
        // запуск функции в фоновом потоке
        thread {
            edited.value?.let {
                repository.save(it)
                // оповещение о результате
                _postCreated.postValue(Unit)
            }
            edited.postValue(empty)
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(post: Post) {
        thread {
            try {
                val updatePost = if (post.likedByMe) {      // если пост был лайкнут
                    repository.unlikeById(post.id)
                } else {                                           // если не лайкнут
                    repository.likeById(post.id)
                }
                // кладём в новый список
                val newPosts = _data.value?.posts?.map {
                    if (it.id == post.id) {              // если id совпадает, берём новый пост
                        updatePost
                    } else {                             // если id не совпадает, берём старый пост
                        it
                    }
                }.orEmpty()                              // пустой список
                _data.postValue(_data.value?.copy(posts = newPosts))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun removeById(id: Long) {
        thread {
            val old = _data.value

            _data.postValue(
                old?.copy(
                    posts = old.posts.filter {
                        it.id != id
                    }
                )
            )
            try {
                repository.removeById(id)
            } catch (e: Exception) {
                _data.postValue(old)
            }
        }
    }
}