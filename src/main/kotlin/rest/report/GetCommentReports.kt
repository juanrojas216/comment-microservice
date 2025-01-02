package rest.report

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.report.ReportRepository
import model.report.dto.ReportData
import model.report.dto.asReportData
import model.security.TokenService
import model.security.validateTokenIsAdminUser
import rest.validations.asObjectId
import rest.validations.asReportCriteriaValidation
import rest.authHeader

/**
 * @api {post} /v1/articles/ Crear Artículo
 * @apiName Crear Artículo
 * @apiGroup Artículos
 *
 * @apiUse AuthHeader
 *
 * @apiExample {json} Body
 *  {
 *      "articleId": "{nombre del articulo}",
 *      "image": "{id de imagen}",
 *      "price": {precio actual},
 *      "stock": {stock actual}
 *  }
 *
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 * {
 *      "_id": "{id de articulo}"
 *      "name": "{nombre del articulo}",
 *      "description": "{descripción del articulo}",
 *      "image": "{id de imagen}",
 *      "price": {precio actual},
 *      "stock": {stock actual}
 *      "updated": {fecha ultima actualización}
 *      "created": {fecha creación}
 *      "enabled": {si esta activo}
 * }
 *
 * @apiUse Errors
 */
class GetCommentReports(
    private val repository: ReportRepository,
    private val tokenService: TokenService
) {
    fun init(app: Routing) = app.apply {
        get("/v1/comments/{commentId}/reports/{criteria}") {
            this.call.authHeader.validateTokenIsAdminUser(tokenService)
            val commentId = this.call.parameters["commentId"].asObjectId
            val criteria = this.call.parameters["criteria"].asReportCriteriaValidation

            val result: List<ReportData> = when (criteria) {
                "all" -> repository.findByCommentId(commentId).map { it.asReportData }
                "unreviewed" -> repository.findByCommentIdUnreviewed(commentId).map { it.asReportData }
                else -> emptyList()
            }

            this.call.respond(result)
        }
    }
}
