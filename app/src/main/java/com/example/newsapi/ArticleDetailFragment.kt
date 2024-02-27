package com.example.newsapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newsapi.databinding.FragmentArticleDetailBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ArticleDetailFragment: Fragment() {
    private var _binding: FragmentArticleDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    // the articleId is passed to ArticleDetailFragment by navigation. Now we need to pass it to ViewModel
    private val args: ArticleDetailFragmentArgs by navArgs()
    // load the ViewModel via ViewModelFactory instead. Because it's gonna accept an argument called 'articleId', which is unusual
    private val articleDetailViewModel: ArticleDetailViewModel by viewModels {
        ArticleDetailViewModeFactory(args.articleParam)
    }

    private lateinit var article: Article

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate the _binding when the fragment is being created
        _binding = FragmentArticleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
// bind some listeners to Button, TextView, ...
        }

        // When the articleDetailFragment is create, bind a getter() to the ViewModel.
        // Whenever the ViewModel is started, the getter() collect article from the flow and set the UI
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // the following will runs every time ViewModel is started
                articleDetailViewModel.article.collect { article: Article? ->
                    article?.let {
                        // update UI based on the article provided by the flow
                        updateUi(it)
//                        binding.articleTitleTextView.text = it.title
//                        binding.articleContentTextView.text = it.content
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(article: Article) {
        binding.articleTitleTextView.text = article.title
        binding.articleSourceTextView.text = getString(R.string.article_source, article.source.name ?: "unknown source")
        binding.articleAuthorTextView.text = getString(R.string.article_author, article.author ?: "unknown author")
        binding.articleContentTextView.text = article.content

        Glide.with(this)
            .load(article.urlToImage)
            .into(binding.imageView)
    }
}