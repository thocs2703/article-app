package vinova.kane.article.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import vinova.kane.article.R
import vinova.kane.article.network.NetworkState

class NetworkStateViewHolder(view: View, retryCallback: () -> Unit) : RecyclerView.ViewHolder(view) {
    private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
    private val retryButton = view.findViewById<Button>(R.id.retry_button)

    init {
        retryButton.setOnClickListener { retryCallback() }
    }

    fun bind(state: NetworkState?) {
        progressBar.visibility = if (state == NetworkState.LOADING) View.VISIBLE else View.GONE
        retryButton.visibility = if (state == NetworkState.ERROR) View.VISIBLE else View.GONE
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateViewHolder {
            return NetworkStateViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.network_state_item, parent, false),
                retryCallback
            )
        }
    }
}