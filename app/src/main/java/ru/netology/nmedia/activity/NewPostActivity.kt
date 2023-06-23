package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editText.requestFocus()
        binding.ok.setOnClickListener {                     // обработчик на кнопку OK
            val intent = Intent()                                   // создаём intent
            if (binding.editText.text.isNullOrBlank()) {            // проверка данных если Null
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.editText.text.toString()      // content из строки ввода
                intent.putExtra(Intent.EXTRA_TEXT, content)         // кладём в intent данные
                setResult(Activity.RESULT_OK, intent)               // результат - OK
            }
            finish()                                                // убираем активити
        }
    }
}