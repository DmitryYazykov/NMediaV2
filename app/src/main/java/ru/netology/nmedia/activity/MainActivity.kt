package ru.netology.nmedia.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.Group
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
        val group: Group = findViewById(R.id.group)
        val deleteButton: ImageButton = findViewById(R.id.deleteButton)   // присваиваю в переменную кнопку "крестик" из макета
        val editText: EditText = findViewById(R.id.postContent)           // присваиваю в переменную поле ввода текста из макета
        var originalText = ""                                             // Переменная для хранения оригинального текста

        viewModel.cancelVisible.observe(this) { visible ->         // наблюдатель для cancelVisible в viewModel
            group.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        }

        deleteButton.setOnClickListener {                         // слушатель на кнопку deleteButton
            editText.setText(originalText)
            AndroidUtils.hideKeyboard(editText)                           // вызываю объект скрытия клавиатуры
        }

        // код для сохранения оригинального текста при начале редактирования
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                originalText = editText.text.toString()
                deleteButton.visibility = View.VISIBLE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.cancelVisible.value =
            false                                                        // Скрытие кнопку "крестик" при паузе
    }
}