package com.example.newsapi

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapi.databinding.FragmentArticleListBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

class ArticleListFragment: Fragment() {
    private var _binding: FragmentArticleListBinding? = null
    private val binding: FragmentArticleListBinding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private val articleListViewModel: ArticleListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleListBinding.inflate(inflater, container, false)
        binding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val handleItemClicked: (articleParam: Article) -> Unit = { articleParam: Article ->
//            Toast.makeText(context,  "article item ${articleParam.title} clicked. Now navigate to ArticleDetail", Toast.LENGTH_SHORT).show()
            Log.d("NavigationDebug", "Article to pass: $articleParam")
            findNavController().navigate(ArticleListFragmentDirections.actionArticleListFragmentToArticleDetailFragment(articleParam))
        }

        // When the articleListFragment is create, bind a getter() to the ViewModel.
        // Whenever the ViewModel is started, the getter() collect article from the flow and set the adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // the following will runs every time ViewModel is started
                Log.d("ArticleListFragmentDebug", "the following will runs every time ViewModel is started")
                setUpSpinnerAdapter()
                articleListViewModel.articles.collect { articles: List<Article> ->
                    setUpRecyclerViewAdapter(articles, handleItemClicked)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecyclerViewAdapter(articles: List<Article>, handleItemClicked: (articleParam: Article) -> Unit) {
        binding.articleRecyclerView.adapter = ArticleListAdapter(articles, handleItemClicked)
    }

    private fun setUpSpinnerAdapter() {
        Log.d("ArticleListFragmentDebug", "setting up spinner adapter...")
        val categories = arrayOf("business", "entertainment", "general", "health", "science", "sports", "technology")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.articleCategorySpinner.adapter = adapter

        binding.articleCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCategory = categories[position]
                articleListViewModel.fetchArticles(selectedCategory)
                Log.d("ArticleListFragmentDebug", "onItemSelected: articleListViewModel.fetchArticles($selectedCategory)")

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                articleListViewModel.fetchArticles()
                Log.d("ArticleListFragmentDebug", "onNothingSelected: articleListViewModel.fetchArticles()")
            }

        }
    }
}
