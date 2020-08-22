package vinova.kane.article.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import vinova.kane.article.R
import vinova.kane.article.network.State

class NetworkStateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
    private val retryButton = view.findViewById<Button>(R.id.retry_button)

    fun bind(state: State?) {
        if (state != null && state == State.LOADING) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }

        if (state != null && state == State.ERROR) {
            retryButton.visibility = View.VISIBLE
        } else {
            retryButton.visibility = View.GONE
        }
    }

    companion object {
        fun create(parent: ViewGroup): NetworkStateViewHolder {
            return NetworkStateViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.network_state_item, parent, false)
            )
        }
    }
}