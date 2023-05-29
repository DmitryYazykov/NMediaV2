package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {

    //объявление репозитория
    val repository: PostRepository = PostRepositoryInMemoryImpl()

    //свойства с данными data
    val data = repository.get()

    //ф-ия лайка - дублирует ф-ию лайк репозитория
    fun like() = repository.like()

    //ф-ия поделиться - дублирует ф-ию поделиться репозитория
    fun share() = repository.share()
}