@file:Suppress("DEPRECATION")

package vinova.kane.article.ui.overview

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import vinova.kane.article.R
import vinova.kane.article.adapter.ArticleAdapter
import vinova.kane.article.databinding.FragmentOverviewBinding
import vinova.kane.article.network.NetworkState
import kotlin.collections.set

class OverviewFragment : Fragment(){

    private lateinit var viewModel: OverviewViewModel
    private lateinit var binding: FragmentOverviewBinding
    private lateinit var searchView: SearchView
    private lateinit var adapter: ArticleAdapter

    private var queries: HashMap<String, String> = hashMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOverviewBinding.inflate(inflater)

        viewModel = ViewModelProviders.of(this).get(OverviewViewModel::class.java)

        initAdapter()
        initState()

        initToolbar()

        binding.retryButton.setOnClickListener { viewModel.retry() }

        return binding.root
    }

    private fun initAdapter() {
        adapter = ArticleAdapter { viewModel.retry() }

        binding.recyclerArticle.adapter = adapter

        viewModel.newArticlePagedList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            Log.d("OverviewFragment", "Adapter's size: ${adapter.itemCount}")
        })
    }

    private fun initToolbar(){
        (activity as AppCompatActivity).setSupportActionBar(binding.toolBar)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        setHasOptionsMenu(true)
    }

    private fun searchArticleList(queries: Map<String, String>){
        viewModel.search(queries)
        initAdapter()
        initState()
    }

    private fun initState(){
        viewModel.getNetworkState().observe(viewLifecycleOwner, Observer<NetworkState> {
            binding.progressBar.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING)
                    View.VISIBLE else View.GONE

            binding.retryButton.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR)
                    View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()){
                adapter.setNetworkState(it ?: NetworkState.DONE)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        val searchItem = menu.findItem(R.id.search_bar)

        if (searchItem != null) {
            @Suppress("DEPRECATION")
            searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    queries[Q_PARAM] = query
                    searchArticleList(queries)
                }
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.filter_bar) {
            findNavController().navigate(R.id.overview_nav_filter)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val Q_PARAM = "q"
    }
}