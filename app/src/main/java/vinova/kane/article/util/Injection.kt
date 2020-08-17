package vinova.kane.article.util

import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import vinova.kane.article.network.ArticleApi
import vinova.kane.article.ui.OverviewViewModelFactory
import vinova.kane.article.repository.ArticleRepository

@ExperimentalCoroutinesApi
object Injection {
    private fun provideArticleRepository(): ArticleRepository{
        return ArticleRepository(ArticleApi.retrofitService)
    }

    fun provideOverviewViewModelFactory(): ViewModelProvider.Factory{
        return OverviewViewModelFactory(provideArticleRepository())
    }
}