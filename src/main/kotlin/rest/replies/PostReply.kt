package rest.replies

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.reply.dto.asReplyData
import model.reply.saveIn
import model.reply.ReplyRepository
import model.reply.dto.NewReplyData
import model.reply.dto.toNewReply
import model.security.TokenService
import model.security.validateTokenIsUserUser
import rabbit.EventNewCommentData
import rest.validations.asObjectId
import rest.authHeader

/**
 * @api {post} /v1/comments/{articleId} Crear respuesta
 * @apiName Crear respuesta
 * @apiGroup Comentarios
 *
 * @apiUse AuthHeader
 *
 * @apiExample {json} Body
 *  {
 *   "content":"{texto de la respuesta}",
 *  }
 *
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 * {
 *      "_id": "{id del comentario}"
 *      "commentId": "{id del comentario}",
 *      "userId": "{id del usuario}",
 *      "content": "{texto de la respuesta}",
 *      "created": {fecha de creacion}
 * }
 *
 * @apiUse Errors
 */
class PostReply(
    private val repository: ReplyRepository,
    private val tokenService: TokenService
) {
    fun init(app: Routing) = app.apply {
        post<NewReplyData>("/v1/comments/{commentId}/reply") {
            val user = this.call.authHeader.validateTokenIsUserUser(tokenService)
            val commentId = this.call.parameters["commentId"].asObjectId

            val reply = it.copy(
                commentId = commentId,
                userId = user.id
            ).toNewReply.saveIn(repository).asReplyData

            this.call.respond(reply)
        }
    }
}
