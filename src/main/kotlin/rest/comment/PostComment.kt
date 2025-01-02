package rest.comment

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.comment.CommentsRepository
import model.comment.dto.NewCommentData
import model.comment.dto.asCommentData
import model.comment.dto.toNewComment
import model.comment.saveIn
import model.security.TokenService
import model.security.validateTokenIsUserUser
import rabbit.EventNewCommentData
import rest.validations.asObjectId
import rest.authHeader

/**
 * @api {post} /v1/comments/{articleId} Crear comentario
 * @apiName Crear comentario
 * @apiGroup Comentarios
 *
 * @apiUse AuthHeader
 *
 * @apiExample {json} Body
 *  {
 *   "content":"{texto del comentario}",
 *   "stars": {estrellas de los comentarios} (1-5)
 *  }
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
class PostComment(
    private val repository: CommentsRepository,
    private val tokenService: TokenService
) {
    fun init(app: Routing) = app.apply {
        post<NewCommentData>("/v1/comments/{articleId}") {
            val user = this.call.authHeader.validateTokenIsUserUser(tokenService)
            val articleId = this.call.parameters["articleId"].asObjectId

            val comment = it.copy(
                articleId = articleId,
                userId = user.id
            ).toNewComment.saveIn(repository).asCommentData

            EventNewCommentData(
                id = comment.id,
                content = comment.content,
                userId = comment.userId,
                articleId = comment.articleId,
                stars = comment.stars
            ).publishOn(
                exchange = "comments",
                queue = "new-comments"
            )

            this.call.respond(comment)
        }
    }
}
