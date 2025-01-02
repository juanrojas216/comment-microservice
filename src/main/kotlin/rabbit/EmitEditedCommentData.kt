package rabbit

import com.google.gson.annotations.SerializedName
import model.comment.Comment
import model.comment.dto.asCommentData
import utils.rabbit.DirectPublisher
import utils.rabbit.RabbitEvent
import utils.validator.*

class EmitEditedCommentData {

    companion object {

        /**
         *
         * @api {direct} comments/event-comment-data Publica edición de comentarios
         *
         * @apiGroup RabbitMQ POST
         *
         * @apiDescription Enviá de mensajes event-comment-data. Publica edición de comentarios
         *
         * @apiSuccessExample {json} Mensaje
         * {
         *      "type": "edited-comment-data",
         *      "message" : {
         *          "id": "{commentid}"
         *          "articleId": "{articleId}",
         *          "userId": "{userId}",
         *          "content": "{content}",
         *          "stars": {stars}
         *      }
         * }
         */
        fun sendCommentData(exchange: String?, queue: String?, send: EventEditedCommentData) {
            val eventToSend = RabbitEvent(
                type = "edited-comment-data",
                message = send
            )
            DirectPublisher.publish(exchange, queue, eventToSend)
        }
    }
}

data class EventEditedCommentData(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("articleId")
    val articleId: String? = null,

    @SerializedName("userId")
    val userId: String? = null,

    @SerializedName("content")
    @Required
    val content: String? = null,

    @SerializedName("stars")
    @Required
    val stars: Int = 0,

    @SerializedName("likesCount")
    @Required
    val likesCount: Int = 0,

    @SerializedName("dislikesCount")
    @Required
    val dislikesCount: Int = 0,
) {
    fun publishOn(exchange: String?, queue: String?) {
        EmitEditedCommentData.sendCommentData(exchange, queue, this)
    }
}

fun Comment.EventEditedCommentData(referenceId: String?): EventEditedCommentData {
    val commentData = this.asCommentData
    return EventEditedCommentData(
        id = commentData.id,
        articleId = commentData.articleId,
        userId = commentData.userId,
        content = commentData.content,
        stars = commentData.stars,
    )
}