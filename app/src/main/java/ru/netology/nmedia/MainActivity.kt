package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    val binding: ActivityMainBinding
        get() = _binding!!

    val formatNumber = FormatNumber

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding =
            ActivityMainBinding.inflate(layoutInflater)      // делаем байдинг, загружаем интерфейс
        setContentView(binding.root)                         // привязка к текущей активити

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. " +
                    "Затем появились курсы по дизайну, разработке, аналитике и управлению. " +
                    "Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. " +
                    "Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. " +
                    "Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            shareByMe = false
        )

        with(binding) {
            author.text = post.author                                 // заголовок
            published.text = post.published                           // дата публикации
            content.text = post.content                               // текст поста
            likeCount.text = formatNumber.format(post.likes)          // количество лайков
            shareCount.text = formatNumber.format(post.share)         // количество репостов
            viewCount.text = formatNumber.format(post.view)           // количество просмотров

            binding.likeButton.setOnClickListener {           // добавляем событие на лайк
                post.likedByMe = !post.likedByMe                      // переключение (туда-обратно)
                setLikeButtonState(post)                              // смена цвета иконки лайка
                if (post.likedByMe) {
                    post.likes++                                      // счётчик лайков +
                } else {
                    post.likes--                                      // счётчик лайков -
                }
                likeCount.text = formatNumber.format(post.likes)
            }
            binding.shareButton.setOnClickListener {          // добавляем событие на репост
                post.share++                                          // счётчик репостов +1
                shareCount.text = formatNumber.format(post.share)
            }
        }
    }

    private fun setLikeButtonState(post: Post) {                      // ф-ия для смены иконки лайка
        if (post.likedByMe) {
            binding.likeButton.setImageResource(R.drawable.ic_liked)
        } else {
            binding.likeButton.setImageResource(R.drawable.ic_likes)
        }
    }
}