package vinova.kane.article.article.overview

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import vinova.kane.article.R
import vinova.kane.article.databinding.FragmentOverviewBinding
import vinova.kane.article.ui.ArticleAdapter
import vinova.kane.article.ui.ArticleLoadStateAdapter
import vinova.kane.article.ui.IOnBackPressed
import vinova.kane.article.ui.OverviewViewModel
import vinova.kane.article.util.Injection

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@Suppress("DEPRECATION")
class OverviewFragment : Fragment(), IOnBackPressed {

    // Lazy means OverviewViewModel created the first time it is used.
    private lateinit var viewModel: OverviewViewModel
    private lateinit var binding: FragmentOverviewBinding
    private lateinit var searchView: SearchView

    private val adapter = ArticleAdapter()

    private var searchJob: Job? = null

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchArticle(query).collect {
                adapter.submitData(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.retryButton.setOnClickListener { adapter.retry() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOverviewBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
//        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this, Injection.provideOverviewViewModelFactory())
            .get(OverviewViewModel::class.java)
//        binding.viewModel = viewModel

        initAdapter()
        search(DEFAULT_QUERY)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolBar)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)


        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        val searchItem = menu.findItem(R.id.search_bar)
        val filterItem = menu.findItem(R.id.filter_bar)

        if(searchItem != null){
            searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    search(query)
                    query.trim().let {
                        if(it.isNotEmpty()){
                            scrollToTop()
                            search(it)
                        }
                    }
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

    private fun initAdapter() {
        binding.recyclerArticle.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ArticleLoadStateAdapter{adapter.retry()},
            footer = ArticleLoadStateAdapter{adapter.retry()}
        )

        adapter.addLoadStateListener {loadState ->
            // only show the list
            binding.recyclerArticle.isVisible = loadState.source.refresh is LoadState.NotLoading
            // show loading spinner during initial load or refresh
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // show retry button if initial load or refresh fails
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error
            binding.retryButton.setOnClickListener { adapter.retry() }
        }
    }


    private fun scrollToTop() {
        lifecycleScope.launch {
            adapter.dataRefreshFlow.collect {
                binding.recyclerArticle.scrollToPosition(0)
            }
        }
    }

    companion object {
        private const val DEFAULT_QUERY = ""
    }

    override fun onBackPressed(): Boolean {
        if (!searchView.isIconified) {
            searchView.setIconifiedByDefault(true)
            return true
        }
        return false
    }
}