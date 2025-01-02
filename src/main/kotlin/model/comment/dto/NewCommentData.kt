package model.comment.dto

import com.google.gson.annotations.SerializedName
import model.comment.Comment
import model.comment.CommentEntity
import utils.validator.*

data class NewCommentData(
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
)

val NewCommentData.toNewComment: Comment
    get() {
        this.validate;

        return Comment(
            CommentEntity(
                articleId = this.articleId,
                userId = this.userId,
                content = this.content,
                stars = this.stars
            )
        )
    }
