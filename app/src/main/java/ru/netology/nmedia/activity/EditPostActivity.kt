package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.netology.nmedia.databinding.ActivityNewPostBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class EditPostActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_POST_ID = "extra_post_id"
        const val EXTRA_EDITED_TEXT = "extra_edited_text"
    }

    private lateinit var binding: ActivityNewPostBinding
    private lateinit var postViewModel: PostViewModel
    private var postId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postViewModel = ViewModelProvider(this)[PostViewModel::class.java]

        postId = intent.getLongExtra(EXTRA_POST_ID, -1)

        val postText = postViewModel.getPostTextById(postId)
        binding.editText.setText(postText)

        binding.save.setOnClickListener {
            val editedText = binding.editText.text.toString()
            val intent = Intent()
            intent.putExtra(EXTRA_EDITED_TEXT, editedText)
            postViewModel.updatePostText(editedText)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}