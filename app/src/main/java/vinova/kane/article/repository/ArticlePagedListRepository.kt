package vinova.kane.article.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import vinova.kane.article.model.Doc
import vinova.kane.article.network.ApiService
import vinova.kane.article.network.State

class ArticlePagedListRepository(private val service: ApiService) {

    private lateinit var articleDataSourceFactory: ArticleDataSourceFactory
    private lateinit var articlePagedList: LiveData<PagedList<Doc>>

    fun fetchArticlePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Doc>>{
        articleDataSourceFactory = ArticleDataSourceFactory(service, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(MAX_PAGE_SIZE)
            .build()

        articlePagedList = LivePagedListBuilder(articleDataSourceFactory, config).build()
        return articlePagedList
    }

    fun getNetworkState(): LiveData<State>{
        return Transformations.switchMap<ArticleDataSource, State>(
            articleDataSourceFactory.articleLiveDataSource, ArticleDataSource::state)
    }

    companion object{
        private const val MAX_PAGE_SIZE = 8
    }

}