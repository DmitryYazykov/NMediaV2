package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.netology.nmedia.util.glideDownloadFullImage

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onShare(post: Post) {}
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {
    private val serverPathUrl = "http://10.0.2.2:9999/"
    private val avatarsPathUrl = "${serverPathUrl}/avatars"
    private val attachmentsUrl = "${serverPathUrl}/images"

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            val avatarUrl = "${avatarsPathUrl}/${post.authorAvatar}"

            if (post.attachment != null) {
                val downloadAttachUrl = "${attachmentsUrl}/${post.attachment.url}"
                //glideDownloadFullImage(downloadAttachUrl, binding.attachment)
                glideDownloadFullImage(downloadAttachUrl, binding.attachment, binding.root.context)
                binding.attachment.visibility = View.VISIBLE }
            else {
                binding.attachment.visibility = View.GONE
            }

            Glide.with(binding.avatar)
                .load(avatarUrl)
                .apply(RequestOptions.circleCropTransform()) // Округленный аватар
                .placeholder(R.drawable.ic_loading_100dp)    // Изображение во время загрузки
                .error(R.drawable.ic_error_100dp)            // Если ошибки загрузки
                .timeout(10_000)                   // Таймаут
                .into(binding.avatar)                        // ImageView для аватара

            // в адаптере
            like.isChecked = post.likedByMe
            like.text = "${post.likes}"

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }

                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
