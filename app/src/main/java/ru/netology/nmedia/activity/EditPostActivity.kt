package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.FragmentNewPostBinding

class EditPostActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_POST_ID = "extra_post_id"
        const val EXTRA_EDITED_TEXT = "extra_edited_text"
    }

    private lateinit var binding: FragmentNewPostBinding
    private var postId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postId = intent.getLongExtra(EXTRA_POST_ID, -1)
        val postText = intent.getStringExtra(EXTRA_EDITED_TEXT)

        val editedText = intent.getStringExtra(EXTRA_EDITED_TEXT)
        binding.editText.setText(editedText)

        binding.editText.setText(postText)

        binding.save.setOnClickListener {
            val editText = binding.editText.text.toString()
            val intent = Intent()
            intent.putExtra(EXTRA_EDITED_TEXT, editText)
            intent.putExtra(EXTRA_POST_ID, postId)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}