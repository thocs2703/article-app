package vinova.kane.article.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import vinova.kane.article.model.Doc
import vinova.kane.article.network.NetworkState
import java.lang.IllegalArgumentException

class ArticleAdapter(private val retryCallback: () -> Unit) : PagedListAdapter<Doc, RecyclerView.ViewHolder>(DiffCallback) {

    private var networkState = NetworkState.LOADING

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ARTICLE_VIEW_TYPE){
            (holder as ArticleViewHolder).bind(getItem(position))
        } else{
            (holder as NetworkStateViewHolder).bind(networkState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("ArticleAdapter", "View Type: $viewType")
        return when(viewType){
            ARTICLE_VIEW_TYPE -> ArticleViewHolder.create(parent)
            NETWORK_STATE_VIEW_TYPE -> NetworkStateViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("Unknown View Type!")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position < super.getItemCount())
            ARTICLE_VIEW_TYPE else NETWORK_STATE_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if(hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean{
        return super.getItemCount() != 0 && (networkState == NetworkState.LOADING || networkState == NetworkState.ERROR)
    }

//    fun setNetworkState(newNetworkState: NetworkState){
//        val prevNetworkState = this.networkState
//        val wasLoadingOrError = hasFooter()
//
//        this.networkState = newNetworkState
//        val isLoadingOrError = hasFooter()
//
//        if(isLoadingOrError != wasLoadingOrError){
//            if (wasLoadingOrError){                         // network item --> article item
//                notifyItemRemoved(super.getItemCount())     // remove progress bar
//            } else{                                         // article item --> network item
//                notifyItemInserted(super.getItemCount())    // add progress bar
//            }
//        } else if(isLoadingOrError && prevNetworkState != newNetworkState){
//            notifyItemChanged(itemCount - 1)        // add the network msg
//        }
//    }

    fun setNetworkState(networkState: NetworkState) {
        this.networkState = networkState
        notifyItemChanged(super.getItemCount())
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