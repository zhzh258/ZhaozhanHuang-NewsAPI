package com.example.newsapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.databinding.ListItemArticleBinding
import java.util.UUID

class ArticleHolder(private val binding: ListItemArticleBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(article: Article, onItemClicked: (articleParam: Article) -> Unit) {
        binding.articleTitle.text = article.title
        binding.articleDate.text = "date: " + article.publishedAt
        binding.articleSource.text = "source: " + article.source.name

        binding.root.setOnClickListener {
            onItemClicked(article)
        }
    }
}

class ArticleListAdapter(
    private val articles: List<Article>,
    private val onItemClicked: (articleParam: Article) -> Unit
): RecyclerView.Adapter<ArticleHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemArticleBinding.inflate(inflater, parent, false)
        return ArticleHolder(binding)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        holder.bind(articles[position], onItemClicked)
    }

}