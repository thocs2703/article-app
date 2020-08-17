package vinova.kane.article.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import vinova.kane.article.repository.ArticleRepository
import java.lang.IllegalArgumentException

class OverviewViewModelFactory(private val repository: ArticleRepository): ViewModelProvider.Factory {
    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(OverviewViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return OverviewViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}