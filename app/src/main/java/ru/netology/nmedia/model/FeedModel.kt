package ru.netology.nmedia.model

import ru.netology.nmedia.dto.Post

data class FeedModel(
    val posts: List<Post> = emptyList(),             // список постов
    val error: Boolean = false,                      // сообщение об ошибке
    val loading: Boolean = false,                    // состояние загрузки
    val empty: Boolean = false,                      // дополнительно - пустой список
)