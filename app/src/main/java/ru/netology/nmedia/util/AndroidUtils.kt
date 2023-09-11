package ru.netology.nmedia.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R

object AndroidUtils {
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
fun glideDownloadFullImage (url:String, view: View)  {
    Glide.with(view)
        .load(url)
        .error(R.drawable.ic_error_100dp)
        .fitCenter()
        .centerCrop()
        .centerInside()
        .timeout(10_000)
        .into(view as ImageView)
}