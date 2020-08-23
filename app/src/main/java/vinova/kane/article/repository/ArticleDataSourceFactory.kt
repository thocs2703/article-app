package vinova.kane.article.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import vinova.kane.article.model.Doc
import vinova.kane.article.network.ApiService

class ArticleDataSourceFactory(
    private val service: ApiService,
    private val compositeDisposable: CompositeDisposable,
    private val queries: Map<String, String>)
    :DataSource.Factory<Int, Doc>(){

    val articleLiveDataSource = MutableLiveData<ArticleDataSource>()

    override fun create(): DataSource<Int, Doc> {
        val articleDataSource = ArticleDataSource(service, compositeDisposable, queries)
        Log.d("ArticleDataSourceFactory" ,"$service")
        Log.d("ArticleDataSourceFactory" ,"$queries")

        articleLiveDataSource.postValue(articleDataSource)
        Log.d("ArticleDataSourceFactory" ,"${articleDataSource.state.value}")
        return articleDataSource
    }
}