package vinova.kane.article.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import vinova.kane.article.network.ApiService
import io.reactivex.disposables.CompositeDisposable
import androidx.paging.PageKeyedDataSource
import io.reactivex.schedulers.Schedulers
import vinova.kane.article.model.Doc
import vinova.kane.article.network.State
import java.util.concurrent.TimeUnit


class ArticleDataSource(private val service: ApiService, private val compositeDisposable: CompositeDisposable)
    :PageKeyedDataSource<Int, Doc>(){

    private val page = FIRST_PAGE
    private val queries: Map<String, String>? = null
    val state: MutableLiveData<State> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Doc>
    ) {
        Log.d("ArticleDataSource", "Call Back: $callback")
        state.postValue(State.LOADING)
        compositeDisposable.add(
            service.getArticles(queries, page)
                .delay(3000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callback.onResult(it.response.docs, null, page + 1)
                    state.postValue(State.DONE)
                },{
                    state.postValue(State.ERROR)
                    Log.d("ArticleDataSource", "Initial Load Failure ${it.message}")
                })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Doc>) {
        state.postValue(State.LOADING)
        compositeDisposable.add(
            service.getArticles(queries, page)
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callback.onResult(it.response.docs, params.key + 1)
                    Log.d("ArticleDataSource", "Page Number: ${params.key}")
                    state.postValue(State.DONE)
                },{
                    state.postValue(State.ERROR)
                    Log.d("ArticleDataSource", "Failure ${it.message}")
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Doc>) {

    }

    companion object{
        private const val FIRST_PAGE = 0
    }
}
