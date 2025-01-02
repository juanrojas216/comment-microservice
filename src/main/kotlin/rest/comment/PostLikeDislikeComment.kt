package rest.comment

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.comment.CommentsRepository
import model.comment.saveIn
import model.security.TokenService
import model.security.validateTokenIsUserUser
import rabbit.EventEditedCommentData
import rest.validations.asCommentAction
import rest.validations.asObjectId
import rest.authHeader
import utils.errors.NotFoundError

/**
 * @api {post} /v1/comments/{commentId}/{action}(like/dislike) Dar like a un comentario
 * @apiName Crear Artículo
 * @apiGroup Artículos
 *
 * @apiUse AuthHeader
 *
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 *
 * @apiUse Errors
 */
class PostLikeDislikeComment(
    private val repository: CommentsRepository,
    private val tokenService: TokenService
) {
    fun init(app: Routing) = app.apply {
        post("/v1/comments/{commentId}/{action}") {
            val user = this.call.authHeader.validateTokenIsUserUser(tokenService)
            val id = this.call.parameters["commentId"].asObjectId
            val action = this.call.parameters["action"].asCommentAction

            val comment = repository.findById(id) ?: throw NotFoundError("id")

            if(action == "like") {
                comment.addLike().saveIn(repository)
            } else if (action == "dislike") {
                comment.addDislike().saveIn(repository)
            }

            EventEditedCommentData(
                id = comment.entity.id,
                userId = comment.entity.userId,
                articleId = comment.entity.articleId,
                content = comment.entity.content,
                stars = comment.entity.stars,
                likesCount = comment.entity.likesCount,
                dislikesCount = comment.entity.dislikesCount
            ).publishOn(
                exchange = "comments",
                queue = "edited-comments"
            )

            this.call.respond(HttpStatusCode.OK)
        }
    }
}
