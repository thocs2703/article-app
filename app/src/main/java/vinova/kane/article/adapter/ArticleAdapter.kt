package vinova.kane.article.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import vinova.kane.article.model.Doc
import vinova.kane.article.network.State

class ArticleAdapter : PagedListAdapter<Doc, RecyclerView.ViewHolder>(DiffCallback) {

    private var networkState: State? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ARTICLE_VIEW_TYPE){
            (holder as ArticleViewHolder).bind(getItem(position))
        } else{
            (holder as NetworkStateViewHolder).bind(networkState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ARTICLE_VIEW_TYPE)
            ArticleViewHolder.create(parent) else NetworkStateViewHolder.create(parent)
    }

    override fun getItemViewType(position: Int): Int {
        return if(isLoadingOrError() && position == itemCount - 1)
            NETWORK_STATE_VIEW_TYPE else ARTICLE_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if(isLoadingOrError()) 1 else 0
    }

    private fun isLoadingOrError(): Boolean{
        return networkState != null && networkState != State.DONE
    }

    fun setNetworkState(newNetworkState: State){
        val prevNetworkState = this.networkState
        val wasLoadingOrError = isLoadingOrError()

        this.networkState = newNetworkState
        val isLoadingOrError = isLoadingOrError()

        if(isLoadingOrError != wasLoadingOrError){
            if (wasLoadingOrError){                         // network item --> article item
                notifyItemRemoved(super.getItemCount())     // remove progress bar
            } else{                                         // article item --> network item
                notifyItemInserted(super.getItemCount())    // add progress bar
            }
        } else if(isLoadingOrError && prevNetworkState != newNetworkState){
            notifyItemChanged(itemCount - 1)        // add the network msg
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Doc>() {
        override fun areItemsTheSame(oldItem: Doc, newItem: Doc): Boolean {
            return oldItem === newItem // true if oldItem & newItem refer the same memory
        }

        override fun areContentsTheSame(oldItem: Doc, newItem: Doc): Boolean {
            return oldItem == newItem // true if oldItem & newItem refer the same content
        }

        const val ARTICLE_VIEW_TYPE = 1
        const val NETWORK_STATE_VIEW_TYPE = 2
    }
}