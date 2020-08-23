package vinova.kane.article.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import vinova.kane.article.R
import vinova.kane.article.model.Doc

class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val titleText = view.findViewById<TextView>(R.id.title_text)
    private val posterImage = view.findViewById<ImageView>(R.id.poster_image)

    private var doc: Doc? = null

    init {
        view.setOnClickListener {
            doc?.webUrl.let { url ->
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                @Suppress("DEPRECATION")
                builder.addDefaultShareMenuItem()
                customTabsIntent.launchUrl(view.context, Uri.parse(url))
            }
        }
    }

    fun bind(doc: Doc?) {
        if (doc == null) {
            val resources = itemView.resources
            titleText.text = resources.getString(R.string.loading)
        } else {
            showArticleData(doc)
        }
    }

    private fun showArticleData(doc: Doc) {
        this.doc = doc
        if(doc.multimedia.isEmpty()){
          posterImage.setImageResource(R.drawable.article)
        } else {
            doc.multimedia[0].url.let {
                Glide.with(itemView)
                    .load(IMAGE_URL + it)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.ic_broken_image)
                    )
                    .into(posterImage)
            }
        }
        titleText.text = doc.headline.main
    }

    companion object {
        private const val IMAGE_URL = "https://www.nytimes.com/"

        fun create(parent: ViewGroup): ArticleViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article, parent, false)
            return ArticleViewHolder(view)
        }
    }
}