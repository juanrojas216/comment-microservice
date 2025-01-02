package rest.comment

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.comment.CommentsRepository
import model.comment.dto.ArticleSummary
import model.comment.dto.asCommentData
import rest.validations.asObjectId

/**
 * @api {get} /v1/comments/{articleId}/summary Traer resumen de comentarios de un artículo
 * @apiName Traer resumen de comentarios de un artículo
 * @apiGroup Articulos
 *
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 * {
 *      "average_stars": {promedio de estrellas de todos los comentarios},
 *      "commments_count": {cantidad de comentarios}
 * }
 *
 * @apiUse Errors
 */
class GetArticleSummary(
    private val repository: CommentsRepository
) {
    fun init(app: Routing) = app.apply {
        get("/v1/comments/{articleId}/summary") {
            val articleId = this.call.parameters["articleId"].asObjectId

            val comments = repository.findByArticleId(articleId).map { it.asCommentData }

            val starsAverage = comments.map { it.stars }.average()
            val commentsCount = comments.size
            val summary = ArticleSummary(
                averageStars = starsAverage,
                commentsCount = commentsCount
            )

            this.call.respond(summary)
        }
    }
}
