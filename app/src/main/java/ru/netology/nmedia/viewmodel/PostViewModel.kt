package ru.netology.nmedia.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.util.SingleLiveEvent

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

    val serverError: Toast = Toast.makeText(
        getApplication(),
        "Ошибка со стороны сервера.\nПерезагрузите ещё раз.",
        Toast.LENGTH_SHORT
    )

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.postValue(FeedModel(loading = true))
        repository.getAllAsync(object : PostRepository.Callback<List<Post>> {
            override fun onSuccess(result: List<Post>) {
                _data.value = FeedModel(posts = result, empty = result.isEmpty())
            }

            override fun onError(exception: java.lang.Exception) {
                if (exception is NumberResponseError) {
                    _data.postValue(
                        FeedModel(
                            codeResponse = exception.code,
                            posts = _data.value?.posts.orEmpty(),
                            serverError = true, error = true
                        )
                    )
                } else {
                    _data.value = FeedModel(error = true)
                }
            }
        })
    }

    fun save() {
        edited.value?.let { post ->
            repository.saveAsync(post, object : PostRepository.Callback<Unit> {
                override fun onSuccess(result: Unit) {
                    _postCreated.postValue(Unit)
                }

                override fun onError(exception: Exception) {
                    serverError.show()
                    _data.postValue(FeedModel(error = true))
                }
            })
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

    fun likePostAsync(likedPost: Post) {
        repository.likePostAsync(likedPost, object : PostRepository.Callback<Post> {
            override fun onSuccess(result: Post) {
                val updatedPosts = _data.value?.posts?.map {
                    if (it.id == result.id) {
                        result
                    } else it
                }.orEmpty()
                _data.postValue(_data.value?.copy(posts = updatedPosts))
            }

            override fun onError(exception: java.lang.Exception) {
                serverError.show()
            }
        })
    }

    fun removeById(id: Long) {
        val old = _data.value
        _data.postValue(
            old?.copy(
                posts = old.posts.filter {
                    it.id != id
                }
            )
        )
        repository.removeAsync(id, object : PostRepository.Callback<Unit> {
            override fun onSuccess(result: Unit) {

            }

            override fun onError(exception: Exception) {
                serverError.show()
                _data.postValue(FeedModel(error = true))
            }
        })
    }
}