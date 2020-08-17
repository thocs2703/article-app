package vinova.kane.article.model


import com.google.gson.annotations.SerializedName

data class Byline(
    @SerializedName("organization")
    val organization: Any,
    @SerializedName("original")
    val original: Any,
    @SerializedName("person")
    val person: List<Any>
)