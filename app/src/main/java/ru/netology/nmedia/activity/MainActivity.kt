package ru.netology.nmedia.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.util.AndroidUtils

class MainActivity : AppCompatActivity() {

    val viewModel: PostViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var originalText = ""

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
        })
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        viewModel.edited.observe(this) { post ->
            if (post.id != 0L) {
                with(binding.postContent) {
                    requestFocus()                                  // фокус ввода
                    setText(post.content)
                }
            }
        }

        binding.saveButton.setOnClickListener {             // обработчик кнопки saveButton
            with(binding.postContent) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        context.getString(R.string.error_empty_content),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    viewModel.changeContent(text.toString())
                    viewModel.save()

                    setText("")
                    clearFocus()
                    AndroidUtils.hideKeyboard(this)
                }
            }
        }

        viewModel.cancelVisible.observe(this) { visible ->         // наблюдатель для cancelVisible в viewModel
            binding.group.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        }

        binding.deleteButton.setOnClickListener {                 // слушатель на кнопку deleteButton
            binding.postContent.setText(originalText)
            AndroidUtils.hideKeyboard(binding.postContent)                // вызываю объект скрытия клавиатуры
            viewModel.cancelEdit()
        }

        // код для сохранения оригинального текста при начале редактирования
        binding.postContent.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                originalText = binding.postContent.text.toString()
                binding.deleteButton.visibility = View.VISIBLE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.cancelVisible.value =
            false                                                        // Скрытие кнопку "крестик" при паузе
    }
}