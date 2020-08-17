package vinova.kane.article.model


import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("copyright")
    val copyright: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("response")
    val response: Response
)