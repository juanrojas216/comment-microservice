package rest.replies

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.reply.dto.asReplyData
import model.reply.ReplyRepository
import rest.validations.asObjectId

/**
 * @api {get} /v1/comments/{articleId} Buscar respuestas de un comentario
 * @apiName Buscar respuestas de un comentario
 * @apiGroup Comentarios
 * @apiDescription Busca respuestas utilizando el id de un comentario
 *
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 * [
 *      {
 *          "_id": "{id del comentario}"
 *          "commentId": "{id del comentario}",
 *          "userId": "{id del usuario}",
 *          "content": "{texto de la respuesta}",
 *          "created": {fecha de creacion}
 *      },
 *      ...
 * ]
 *
 * @apiUse Errors
 */
class GetCommentReplies(
    private val repository: ReplyRepository
) {
    fun init(app: Routing) = app.apply {
        get("/v1/comments/{commentId}/replies/{replyId}") {
            val commentId = this.call.parameters["commentId"].asObjectId
            val replyId = this.call.parameters["replyId"].asObjectId

            val result = repository.findByCommentId(commentId).map { it.asReplyData }
            this.call.respond(result)
        }
    }
}