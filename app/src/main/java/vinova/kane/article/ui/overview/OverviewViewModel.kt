package vinova.kane.article.ui.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import vinova.kane.article.model.Article
import vinova.kane.article.model.Doc
import vinova.kane.article.network.Client
import vinova.kane.article.network.NetworkState
import vinova.kane.article.repository.ArticleDataSource
import vinova.kane.article.repository.ArticleDataSourceFactory

class OverviewViewModel() : ViewModel() {
    private var sourceFactory: ArticleDataSourceFactory
    private val compositeDisposable = CompositeDisposable()
    private var config: PagedList.Config
    private val apiService = Client.getClient()
    var newArticlePagedList: LiveData<PagedList<Doc>>

    init {
        sourceFactory = ArticleDataSourceFactory(apiService, compositeDisposable, hashMapOf())

        config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE * 2)
            .setEnablePlaceholders(false)
            .build()

        newArticlePagedList = LivePagedListBuilder<Int, Doc>(sourceFactory, config).build()
    }

    fun search(queries: Map<String, String>){
        sourceFactory = ArticleDataSourceFactory(apiService, compositeDisposable, queries)
        newArticlePagedList = LivePagedListBuilder<Int, Doc>(sourceFactory, config).build()
    }

    fun retry() {
        sourceFactory.articleLiveDataSource.value?.retry()
    }

    fun getNetworkState(): LiveData<NetworkState> =
        Transformations.switchMap<ArticleDataSource, NetworkState>(sourceFactory.articleLiveDataSource) {
            it.state
        }

    fun refresh() {
        sourceFactory.articleLiveDataSource.value!!.invalidate()
    }

    fun getRefreshState(): LiveData<NetworkState> =
        Transformations.switchMap<ArticleDataSource, NetworkState>(sourceFactory.articleLiveDataSource) {
            it.initialLoad
        }

    fun listIsEmpty(): Boolean {
        return newArticlePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}
