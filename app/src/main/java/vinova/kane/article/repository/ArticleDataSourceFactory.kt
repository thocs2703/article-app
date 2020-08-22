package vinova.kane.article.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import vinova.kane.article.model.Doc
import vinova.kane.article.network.ApiService

class ArticleDataSourceFactory(private val service: ApiService, private val compositeDisposable: CompositeDisposable)
    :DataSource.Factory<Int, Doc>(){

    val articleLiveDataSource = MutableLiveData<ArticleDataSource>()

    override fun create(): DataSource<Int, Doc> {
        val articleDataSource = ArticleDataSource(service, compositeDisposable)

        articleLiveDataSource.postValue(articleDataSource)
        return articleDataSource
    }
}