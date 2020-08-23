package vinova.kane.article.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import vinova.kane.article.network.ApiService
import io.reactivex.disposables.CompositeDisposable
import androidx.paging.PageKeyedDataSource
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import vinova.kane.article.model.Doc
import vinova.kane.article.network.NetworkState
import java.util.concurrent.TimeUnit


class ArticleDataSource(
    private val service: ApiService,
    private val compositeDisposable: CompositeDisposable,
    private val queries: Map<String, String>)
    :PageKeyedDataSource<Int, Doc>(){

    private val page = FIRST_PAGE

    val state: MutableLiveData<NetworkState> = MutableLiveData()
    val initialLoad: MutableLiveData<NetworkState> = MutableLiveData()

    private var retryCompletable: Completable? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Doc>
    ) {
        state.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            service.getArticles(queries, page)
                .subscribe({
                    callback.onResult(it.response.docs, null, page + 1)
                    Log.d("ArticleDataSource", "Doc's size: ${it.response.docs.size}")
                    Log.d("ArticleDataSource", "Queries: $queries")
                    state.postValue(NetworkState.DONE)
                    initialLoad.postValue(NetworkState.DONE)
                },{
                    setRetry(Action { loadInitial(params, callback) })
                    state.postValue(NetworkState.ERROR)
                    initialLoad.postValue(NetworkState.ERROR)
                    Log.d("ArticleDataSource", "Initial Load Failure ${it.message}")
                })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Doc>) {
        state.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            service.getArticles(queries, params.key)
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribe({
                    callback.onResult(it.response.docs, params.key + 1)
                    Log.d("ArticleDataSource", "Page Number: ${params.key}")
                    state.postValue(NetworkState.DONE)
                },{
                    setRetry(Action { loadAfter(params, callback) })
                    state.postValue(NetworkState.ERROR)
                    Log.d("ArticleDataSource", "Failure ${it.message}")
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Doc>) {

    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

    companion object{
        private const val FIRST_PAGE = 0
    }
}
