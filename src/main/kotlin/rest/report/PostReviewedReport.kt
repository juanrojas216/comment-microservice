package rest.report

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.report.ReportRepository
import model.report.saveIn
import model.security.TokenService
import model.security.validateTokenIsAdminUser
import rest.validations.asObjectId
import rest.authHeader
import utils.errors.NotFoundError

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
class PostReviewedReport(
    private val repository: ReportRepository,
    private val tokenService: TokenService
) {
    fun init(app: Routing) = app.apply {
        post("/v1/reports/{reportId}/reviewed") {
            this.call.authHeader.validateTokenIsAdminUser(tokenService)
            val id = this.call.parameters["reportId"].asObjectId

            (repository.findById(id) ?: throw NotFoundError("id"))
                .setReviwed().saveIn(repository)

            this.call.respond(HttpStatusCode.OK)
        }
    }
}
