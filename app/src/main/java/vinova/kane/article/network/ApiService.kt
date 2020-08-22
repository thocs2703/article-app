package vinova.kane.article.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import vinova.kane.article.model.Article


interface ApiService {
    @GET("articlesearch.json")
    fun getArticles(
        @QueryMap queries: Map<String, String>? = null,
        @Query("page") pageNumber: Int,
        @Query("api-key") apiKey: String = API_KEY
    ):
            Single<Article>

    companion object{
        private const val API_KEY = "KWdvr5UFCidpgKsSryXkSG3FDvj0cVQd"
    }
}


