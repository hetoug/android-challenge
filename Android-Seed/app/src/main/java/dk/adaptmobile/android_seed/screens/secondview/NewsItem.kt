package dk.adaptmobile.android_seed.screens.secondview

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.model.NewsApiArticle
import dk.adaptmobile.android_seed.navigation.NavManager
import dk.adaptmobile.android_seed.screens.Routing
import kotlinx.android.synthetic.main.view_news_list_item.view.*

class NewsItem(private val article: NewsApiArticle) : Item() {
    override fun getLayout(): Int = R.layout.view_news_list_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.newsListItemTitle.text = article.title
        viewHolder.itemView.newsListItemDescription.text = article.description
        viewHolder.itemView.newsListItemUrl.text = article.url
        viewHolder.itemView.setOnClickListener {
            if (it.newsListItemUrl.text != null) {
                NavManager.open(Routing.Browser(it.newsListItemUrl.text.toString()))
            }
        }
    }
}