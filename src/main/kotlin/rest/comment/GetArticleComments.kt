package rest.comment

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.comment.CommentsRepository
import model.comment.dto.asCommentData
import rest.validations.asObjectId

/**
 * @api {get} /v1/comments/{articleId} Buscar comentarios de un articulo
 * @apiName Buscar comentarios de un articulo
 * @apiGroup Comentarios
 * @apiDescription Busca comentarios utilizando el id de un articulo
 *
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 * [
 *      {
 *          "_id": "{id del comentario}"
 *          "articleId": "{id del comentario}",
 *          "userId": "{id del usuario autor}",
 *          "content": "{texto del comentario}",
 *          "likesCount": {precio actual},
 *          "dislikesCount": {stock actual}
 *          "stars": {estrellas de los comentarios} (1-5)
 *          "created": {fecha de creacion}
 *          "updated": {fecha de última modificación}
 *      },
 *      ...
 * ]
 *
 * @apiUse Errors
 */
class GetArticleComments(
    private val repository: CommentsRepository
) {
    fun init(app: Routing) = app.apply {
        get("/v1/comments/{articleId}") {
            val articleId = this.call.parameters["articleId"].asObjectId

            val result = repository.findByArticleId(articleId).map { it.asCommentData }
            this.call.respond(result)
        }
    }
}