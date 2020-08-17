package vinova.kane.article.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import vinova.kane.article.model.Article
import vinova.kane.article.util.Constant


private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(Constant.BASE_URL)
    .build()

interface ArticleApiService {
    @GET("articlesearch.json")
    suspend fun getArticleProperties(
        @Query("q") query: String,
        @Query("page") pageNumber: Int,
        @Query("api-key") api_key: String = Constant.API_KEY
    ):
            Article
}

object ArticleApi {
    val retrofitService: ArticleApiService by lazy {
        retrofit.create(ArticleApiService::class.java)
    }
}

