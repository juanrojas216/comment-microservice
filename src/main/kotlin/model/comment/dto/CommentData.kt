package model.comment.dto

import com.google.gson.annotations.SerializedName
import model.comment.Comment
import utils.validator.*
import java.util.*

data class CommentData(
    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("articleId")
    val articleId: String? = null,

    @SerializedName("userId")
    val userId: String? = null,

    @SerializedName("content")
    @Required
    @MinLen(10)
    @MaxLen(2048)
    val content: String? = null,

    @SerializedName("stars")
    @Required
    @MaxValue(5)
    @MinValue(1)
    val stars: Int = 0,

    @SerializedName("likesCount")
    val likesCount: Int = 0,

    @SerializedName("dislikesCount")
    val dislikesCount: Int = 0,

    @SerializedName("created")
    val created: Date? = null,

    @SerializedName("updated")
    val updated: Date? = null
)

val Comment.asCommentData
    get() = CommentData(
        id = this.entity.id,
        articleId = this.entity.articleId,
        userId = this.entity.userId,
        content = this.entity.content,
        likesCount = this.entity.likesCount,
        dislikesCount = this.entity.dislikesCount,
        stars = this.entity.stars,
        created = this.entity.created,
        updated = this.entity.updated
    )