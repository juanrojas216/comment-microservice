package rabbit

import com.google.gson.annotations.SerializedName
import model.comment.Comment
import model.comment.dto.asCommentData
import utils.rabbit.DirectPublisher
import utils.rabbit.RabbitEvent
import utils.validator.*

class EmitNewCommentData {

    companion object {

        /**
         *
         * @api {direct} comments/event-comment-data Publica comentarios
         *
         * @apiGroup RabbitMQ POST
         *
         * @apiDescription Enviá de mensajes event-comment-data. Publica comentarios
         *
         * @apiSuccessExample {json} Mensaje
         * {
         *      "type": "new-comment-data",
         *      "message" : {
         *          "id": "{commentid}"
         *          "articleId": "{articleId}",
         *          "userId": "{userId}",
         *          "content": "{content}",
         *          "stars": {stars}
         *      }
         * }
         */
        fun sendCommentData(exchange: String?, queue: String?, send: EventNewCommentData) {
            val eventToSend = RabbitEvent(
                type = "new-comment-data",
                message = send
            )
            DirectPublisher.publish(exchange, queue, eventToSend)
        }
    }
}

data class EventNewCommentData(
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
) {
    fun publishOn(exchange: String?, queue: String?) {
        EmitNewCommentData.sendCommentData(exchange, queue, this)
    }
}

fun Comment.asEventNewCommentData(referenceId: String?): EventNewCommentData {
    val commentData = this.asCommentData
    return EventNewCommentData(
        id = commentData.id,
        articleId = commentData.articleId,
        userId = commentData.userId,
        content = commentData.content,
        stars = commentData.stars,
    )
}