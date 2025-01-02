package rest.comment

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.comment.CommentsRepository
import model.comment.dto.asCommentData
import utils.errors.ValidationError

/**
 * @api {get} /v1/comments/search/{criteria} Buscar comentarios
 * @apiName Buscar comentarios
 * @apiGroup Comentarios
 * @apiDescription Buscar comentarios por contenido textual
 *
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 * [
 *      {
 *      "_id": "{id del comentario}"
 *      "articleId": "{id del comentario}",
 *      "userId": "{id del usuario autor}",
 *      "content": "{texto del comentario}",
 *      "likesCount": {precio actual},
 *      "dislikesCount": {stock actual}
 *      "stars": {estrellas de los comentarios} (1-5)
 *      "created": {fecha de creacion}
 *      "updated": {fecha de última modificación}
 *      },
 *      ...
 * ]
 *
 * @apiUse Errors
 */
class GetCommentsSearchCriteria(
    private val repository: CommentsRepository
) {
    fun init(app: Routing) = app.apply {
        get("/v1/comments/search/{criteria}") {
            val criteria = this.call.parameters["criteria"].validateAsSearchCriteria()

            val response = repository.findByCriteria(criteria).map { it.asCommentData }
            this.call.respond(response)
        }
    }
}

private fun String?.validateAsSearchCriteria(): String {
    if (this.isNullOrBlank()) {
        throw ValidationError("criteria" to "Must be provided")
    }
    return this
}