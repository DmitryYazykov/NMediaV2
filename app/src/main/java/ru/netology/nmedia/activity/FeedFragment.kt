package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {
    //private lateinit var binding: ActivityMainBinding

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
//
//    private val updatePostLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val editedText = result.data?.getStringExtra(EditPostActivity.EXTRA_EDITED_TEXT)
//                editedText?.let { text ->
//                    val postId = result.data?.getLongExtra(EditPostActivity.EXTRA_POST_ID, -1)
//                    if (postId != null) {
//                        viewModel.getPostById(postId)?.let { post ->
//                            viewModel.changeContent(text)
//                            viewModel.save()
//                        }
//                    }
//                }
//            }
//        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(layoutInflater, container, false)

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                // редактирование поста
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply
                    { textArg = post.content })
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }
        })
//        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
//            result ?: return@registerForActivityResult
//            viewModel.changeContent(result)
//            viewModel.save()
//        }
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }
        return binding.root
    }

//    private fun openEditPostActivity(post: Post?) {
//        val intent = Intent(this, EditPostActivity::class.java)
//        post?.let {
//            intent.putExtra(EditPostActivity.EXTRA_POST_ID, it.id)
//            intent.putExtra(EditPostActivity.EXTRA_EDITED_TEXT, it.content)
//        }
//        updatePostLauncher.launch(intent)
//    }
}