package model.reply.dto

import com.google.gson.annotations.SerializedName
import model.reply.Reply
import model.reply.ReplyEntity
import utils.validator.*

data class NewReplyData(
    @SerializedName("articleId")
    val commentId: String? = null,

    @SerializedName("userId")
    val userId: String? = null,

    @SerializedName("content")
    @Required
    @MinLen(10)
    @MaxLen(2048)
    val content: String? = null,
)

val NewReplyData.toNewReply: Reply
    get() {
        this.validate;

        return Reply(
            ReplyEntity(
                commentId = this.commentId,
                userId = this.userId,
                content = this.content,
            )
        )
    }
