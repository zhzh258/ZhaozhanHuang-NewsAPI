package com.example.newsapi

import android.app.Application

class NewsApiApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        ArticleRepository.initialize(this)
    }
}