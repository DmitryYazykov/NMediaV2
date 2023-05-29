package ru.netology.nmedia.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)         // делаем байдинг, загружаем интерфейс
        setContentView(binding.root)                                      // привязка к текущей активити

        val viewModel by viewModels<PostViewModel>()                      // получаем view model
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author                                 // заголовок
                published.text = post.published                           // дата публикации
                content.text = post.content                               // текст поста
                likeCount.text = FormatNumber.format(post.likes)          // количество лайков
                shareCount.text = FormatNumber.format(post.share)         // количество репостов
                viewCount.text = FormatNumber.format(post.view)           // количество просмотров
                likeButton.setImageResource(                              // изменение кнопки лайка
                    if (post.likedByMe) R.drawable.ic_liked
                    else R.drawable.ic_likes
                )
            }
            binding.likeButton.setOnClickListener {               // добавляем событие на лайк
                viewModel.like()
            }
            binding.shareButton.setOnClickListener {              // добавляем событие на репост
                viewModel.share()
            }
        }
    }
}