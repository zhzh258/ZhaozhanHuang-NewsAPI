package com.example.newsapi

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class NewsApiResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

interface NewsApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("category") category: String? = null,
        @Query("apiKey") apiKey: String
    ): NewsApiResponse // Assuming ArticlesResponse is the data class for the JSON response
}

object RetrofitInstance {
    private const val BASE_URL = "https://newsapi.org/"
    val newsApiService: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }
}

// singleton pattern. Remember to initialize it in NewsApiApplication.kt
// initialize(): initialize the singleton object
// get(): get the singleton object
class ArticleRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope,
    private val newsApiService: NewsApiService = RetrofitInstance.newsApiService
){
    companion object {
        private var INSTANCE: ArticleRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = ArticleRepository(context)
            }
        }

        fun get(): ArticleRepository {
            return INSTANCE
                ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }

    // TODO: functionality of this Repo
    fun fetchArticles(category: String? = null): Flow<List<Article>> = flow {
        val response = newsApiService.getTopHeadlines(category = category, apiKey = "b3ce74a634174501976e60c0e7dc4930")
        if (response.status == "ok") {
            emit(response.articles)
        } else {
            Log.e("ArticleRepository", "Failed to fetch articles")
        }
    }
}

