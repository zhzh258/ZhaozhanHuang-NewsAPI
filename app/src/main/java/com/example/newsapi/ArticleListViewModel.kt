package com.example.newsapi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticleListViewModel: ViewModel() {

    private val articleRepository = ArticleRepository.get()

    private val _articles: MutableStateFlow<List<Article>> = MutableStateFlow(emptyList())
    val articles: StateFlow<List<Article>>
        get() = _articles.asStateFlow()

    init {
        fetchArticles()
//        loadDummyData()
    }

    fun fetchArticles(category: String? = null) {
        viewModelScope.launch {
            articleRepository.fetchArticles(category).collect {
                _articles.value = it
            }
        }
    }

    private fun loadDummyData() {
        val dummyArticles = List(10) { i ->
            Article(
                source = Source(
                    id = "$i",
                    name = "$i"
                ),
                author = "$i",
                title = "$i",
                description = "$i",
                url = "$i",
                urlToImage = "$i",
                publishedAt = "$i",
                content = "$i"
            )
        }
        viewModelScope.launch {
            _articles.value = dummyArticles
        }
    }
}