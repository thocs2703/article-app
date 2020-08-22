package vinova.kane.article.overview

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import vinova.kane.article.R
import vinova.kane.article.adapter.ArticleAdapter
import vinova.kane.article.databinding.FragmentOverviewBinding
import vinova.kane.article.network.Client
import vinova.kane.article.network.State
import vinova.kane.article.repository.ArticlePagedListRepository
import vinova.kane.article.util.IOnBackPressed

class OverviewFragment : Fragment(), IOnBackPressed {

    private lateinit var viewModel: OverviewViewModel
    private lateinit var articleRepository: ArticlePagedListRepository
    private val apiService = Client.getClient()

    private lateinit var binding: FragmentOverviewBinding
    private lateinit var searchView: SearchView

    private var queries: HashMap<String, String> = hashMapOf()

    private val adapter = ArticleAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOverviewBinding.inflate(inflater)

        articleRepository = ArticlePagedListRepository(apiService)
        viewModel = getViewModel()

        val gridLayoutManager = GridLayoutManager(context, 4)
        gridLayoutManager.spanSizeLookup = object :GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return if(adapter.getItemViewType(position) == ArticleAdapter.ARTICLE_VIEW_TYPE)
                    1 else 4
            }
        }

        searchArticle()

        binding.recyclerArticle.layoutManager = gridLayoutManager
        binding.recyclerArticle.adapter = adapter

        (activity as AppCompatActivity).setSupportActionBar(binding.toolBar)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)


        setHasOptionsMenu(true)
        return binding.root
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

    private fun searchArticle(){
        viewModel.articlePagedList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility =
                if (viewModel.listIsEmpty() && it == State.LOADING)
                    View.VISIBLE else View.GONE

            binding.retryButton.visibility =
                if (viewModel.listIsEmpty() && it == State.ERROR)
                    View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()){
                adapter.setNetworkState(it)
            }
        })

    }



    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    private fun getViewModel(): OverviewViewModel{
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return OverviewViewModel(articleRepository) as T
            }
        })[OverviewViewModel::class.java]
    }

    override fun onBackPressed(): Boolean {
        if (!searchView.isIconified) {
            searchView.setIconifiedByDefault(true)
            return true
        }
        return false
    }

    companion object {
        private const val Q_PARAM = "q"
    }
}