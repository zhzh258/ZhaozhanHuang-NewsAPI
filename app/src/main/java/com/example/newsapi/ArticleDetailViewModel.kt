package com.example.newsapi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.newsapi.databinding.FragmentArticleDetailBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// This ViewModel accepts a string as parameter. This is unusual and can be done by ViewModelProvider.Factory
class ArticleDetailViewModel(articleParam: Article): ViewModel() {
//    private val articleApi = ArticleApi.get()
    private val _article: MutableStateFlow<Article?> = MutableStateFlow(null)
    val article: StateFlow<Article?> = _article.asStateFlow()

    init {
        // TODO: build the ArticleApi.kt and fetch the article with given id
//        loadDummyData(articleId)
        _article.value = articleParam
    }
}

class ArticleDetailViewModeFactory(private val articleParam: Article): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ArticleDetailViewModel(articleParam) as T
    }
}