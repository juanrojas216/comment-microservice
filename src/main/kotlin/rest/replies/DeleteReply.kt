package rest.replies

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.comment.CommentsRepository
import model.comment.saveIn
import model.security.TokenService
import model.security.validateTokenIsLoggedIn
import rest.validations.asObjectId
import rest.authHeader
import utils.errors.NotFoundError

/**
 * @api {delete} /v1/comments/{commentId}/replies/{replyId} Eliminar respuesta
 * @apiName Eliminar respuesta
 * @apiGroup Comentarios
 *
 * @apiUse AuthHeader
 *
 * @apiSuccessExample {json} 200 Respuesta
 * HTTP/1.1 200 OK
 *
 * @apiUse Errors
 */
class DeleteReplyId(
    private val repository: CommentsRepository,
    private val tokenService: TokenService
) {
    fun init(app: Routing) = app.apply {
        delete("/v1/comments/{commentId}/replies/{replyId}") {
            val user = this.call.authHeader.validateTokenIsLoggedIn(tokenService)
            val commentId = this.call.parameters["commentId"].asObjectId
            val replyId= this.call.parameters["replyId"].asObjectId

            val reply = repository.findById(replyId) ?: throw NotFoundError("id")

            if (user.permissions?.contains("admin") != true && reply.entity.userId != user.id) {
                this.call.respond(HttpStatusCode.Forbidden, "You do not have permission to delete this comment")
                return@delete
            }

            reply.delete().saveIn(repository)

            this.call.respond(HttpStatusCode.OK)
        }
    }
}