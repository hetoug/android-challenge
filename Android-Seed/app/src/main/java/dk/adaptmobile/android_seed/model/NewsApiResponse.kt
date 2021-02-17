package dk.adaptmobile.android_seed.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsApiResponse(
        val status: String,
        val totalResults: Int,
        val articles: List<NewsApiArticle>
)

@JsonClass(generateAdapter = true)
data class NewsApiArticle(
        val title: String,
        val description: String?,
        val url: String?,
        val urlToImage: String?
)