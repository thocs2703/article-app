package vinova.kane.article.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import vinova.kane.article.repository.ArticlePagedListRepository
import java.lang.IllegalArgumentException
@ExperimentalCoroutinesApi
class OverviewViewModelFactory constructor(private val articleRepository: ArticlePagedListRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(OverviewViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return OverviewViewModel(articleRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}