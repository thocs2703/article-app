package vinova.kane.article.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import vinova.kane.article.R
import vinova.kane.article.databinding.ArticleLoadStateFooterViewItemBinding
import androidx.core.view.isVisible as isVisible

class ArticleLoadStateViewHolder(
    private val binding: ArticleLoadStateFooterViewItemBinding,
    retry: () -> Unit
): RecyclerView.ViewHolder(binding.root) {
    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState){
        if(loadState is LoadState.Error){
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState !is LoadState.Loading
        binding.errorMsg.isVisible = loadState !is LoadState.Loading
    }

    companion object{
        fun create(parent: ViewGroup, retry: () -> Unit): ArticleLoadStateViewHolder{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.article_load_state_footer_view_item, parent, false)
            val binding = ArticleLoadStateFooterViewItemBinding.bind(view)
            return ArticleLoadStateViewHolder(binding, retry)
        }
    }
}