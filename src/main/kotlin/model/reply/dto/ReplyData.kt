package model.reply.dto

import com.google.gson.annotations.SerializedName
import model.reply.Reply
import utils.validator.*
import java.util.*

data class ReplyData(
    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("commentId")
    val commentId: String? = null,

    @SerializedName("userId")
    val userId: String? = null,

    @SerializedName("content")
    @Required
    @MinLen(10)
    @MaxLen(2048)
    val content: String? = null,

    @SerializedName("created")
    val created: Date? = null,
)

val Reply.asReplyData
    get() = ReplyData(
        id = this.entity.id,
        commentId = this.entity.commentId,
        userId = this.entity.userId,
        content = this.entity.content,
        created = this.entity.created
    )