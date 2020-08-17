package vinova.kane.article.model


import com.google.gson.annotations.SerializedName

data class Headline(
    @SerializedName("content_kicker")
    val contentKicker: Any,
    @SerializedName("kicker")
    val kicker: Any,
    @SerializedName("main")
    val main: String,
    @SerializedName("name")
    val name: Any,
    @SerializedName("print_headline")
    val printHeadline: Any,
    @SerializedName("seo")
    val seo: Any,
    @SerializedName("sub")
    val sub: Any
)