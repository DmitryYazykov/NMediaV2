package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

// контракт для нового поста
class NewPostResultContract : ActivityResultContract<String?, String?>() {

    // контракт создаёт intent и его вызывает
    override fun createIntent(context: Context, input: String?): Intent =
        Intent(context, NewPostActivity::class.java)

    // обработка получаемого результата
    override fun parseResult(resultCode: Int, intent: Intent?): String? =
        if (resultCode == Activity.RESULT_OK) {
            intent?.getStringExtra(Intent.EXTRA_TEXT)
        } else {
            null
        }
}