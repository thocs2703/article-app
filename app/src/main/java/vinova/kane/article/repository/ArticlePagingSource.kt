package vinova.kane.article.repository

import android.app.DownloadManager
import android.util.Log
import androidx.paging.PagingSource
import kotlinx.coroutines.CoroutineScope
import retrofit2.HttpException
import vinova.kane.article.model.Article
import vinova.kane.article.model.Doc
import vinova.kane.article.network.ArticleApiService
import java.io.IOException

// Paging Source need to define:
// 1. type of paging key: numbers of page -> Int
// 2. type of data loaded: List<Doc>
// 3. where is data retrieved from: ArticleApiService

private const val ARTICLE_STARTING_PAGE_INDEX = 0

class ArticlePagingSource(
    private val service: ArticleApiService,
    private val query: String
) : PagingSource<Int, Doc>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Doc> {
        val position = params.key ?: ARTICLE_STARTING_PAGE_INDEX
        Log.d("ArticlePagingSource", "Page Number: $position")
        return try {
            val result = service.getArticleProperties(query, position)
            val docs = result.response.docs
            Log.d("ArticlePagingSource", "Doc's size: ${docs.size}")
            LoadResult.Page(
                data = docs,
                prevKey = if (position == ARTICLE_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (docs.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}