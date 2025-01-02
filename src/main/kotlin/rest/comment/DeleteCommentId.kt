package rest.comment

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
 * @api {delete} /comments/{commentId} Eliminar comentario
 * @apiName Eliminar comentario
 * @apiGroup Comentarios
 *
 * @apiUse AuthHeader
 *
 * @apiSuccessExample {json} 200 Respuesta
 * HTTP/1.1 200 OK
 *
 * @apiUse Errors
 */
class DeleteCommentId(
    private val repository: CommentsRepository,
    private val tokenService: TokenService
) {
    fun init(app: Routing) = app.apply {
        delete("/v1/comments/{commentId}") {
            val user = this.call.authHeader.validateTokenIsLoggedIn(tokenService)
            val id = this.call.parameters["commentId"].asObjectId

            val comment = repository.findById(id) ?: throw NotFoundError("id")

            if (user.permissions?.contains("admin") != true && comment.entity.userId != user.id) {
                this.call.respond(HttpStatusCode.Forbidden, "You do not have permission to delete this comment")
                return@delete
            }

            comment.delete().saveIn(repository)

            this.call.respond(HttpStatusCode.OK)
        }
    }
}