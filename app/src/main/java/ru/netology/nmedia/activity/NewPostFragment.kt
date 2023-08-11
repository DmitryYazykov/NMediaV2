package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.StringProperty
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(layoutInflater, container, false)
        arguments?.textArg?.let { binding.editText.setText(it) }
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        binding.editText.requestFocus()
        binding.save.setOnClickListener {                     // обработчик на кнопку Save
            val content = binding.editText.text.toString()     // content из строки ввода
            if (content.isNotBlank()) {                 // проверка текста, сохранение во view-model
                viewModel.changeContent(content)
                viewModel.save()
            }
            findNavController().navigateUp()            // иначе - выходим
        }
        return binding.root
    }

    companion object {
        var Bundle.textArg by StringProperty
    }
}