package rest.comment

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.comment.CommentsRepository
import model.comment.dto.asCommentData
import rest.validations.asObjectId
import utils.errors.NotFoundError

/**
 * @api {get} /v1/comments/{commentId} Buscar comentario
 * @apiName Buscar comentario
 * @apiGroup Comentarios
 *
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 * {
 *      "_id": "{id del comentario}"
 *      "articleId": "{id del comentario}",
 *      "userId": "{id del usuario autor}",
 *      "content": "{texto del comentario}",
 *      "likesCount": {precio actual},
 *      "dislikesCount": {stock actual}
 *      "stars": {estrellas de los comentarios} (1-5)
 *      "created": {fecha de creacion}
 *      "updated": {fecha de última modificación}
 * }
 *
 * @apiUse Errors
 */
class GetCommentId(
    private val repository: CommentsRepository
) {
    fun init(app: Routing) = app.apply {
        get("/v1/comments/{commentId}") {
            val id = this.call.parameters["commentId"].asObjectId

            val data = (repository.findById(id) ?: throw NotFoundError("commentId"))
                .asCommentData

            this.call.respond(data)
        }
    }
}
