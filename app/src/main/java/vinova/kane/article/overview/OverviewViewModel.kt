package vinova.kane.article.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import vinova.kane.article.model.Doc
import vinova.kane.article.network.State
import vinova.kane.article.repository.ArticlePagedListRepository

class OverviewViewModel(private val articleRepository: ArticlePagedListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val articlePagedList: LiveData<PagedList<Doc>> by lazy {
        articleRepository.fetchArticlePagedList(compositeDisposable)
    }

    val networkState: LiveData<State> by lazy {
        articleRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return articlePagedList.value?.isEmpty()?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
