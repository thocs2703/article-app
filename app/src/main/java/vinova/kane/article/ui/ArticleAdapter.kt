package vinova.kane.article.ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import vinova.kane.article.model.Doc

class ArticleAdapter : PagingDataAdapter<Doc, RecyclerView.ViewHolder>(DiffCallback) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val docItem = getItem(position)
        if(docItem != null){
            (holder as ArticleViewHolder).bind(docItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ArticleViewHolder.create(parent)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Doc>() {
        override fun areItemsTheSame(oldItem: Doc, newItem: Doc): Boolean {
            return oldItem === newItem // true if oldItem & newItem refer the same memory
        }

        override fun areContentsTheSame(oldItem: Doc, newItem: Doc): Boolean {
            return oldItem == newItem // true if oldItem & newItem refer the same content
        }

    }
}