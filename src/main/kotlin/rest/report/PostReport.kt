package rest.report

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.report.ReportRepository
import model.report.dto.NewReportData
import model.report.dto.asReportData
import model.report.dto.toNewReport
import model.report.saveIn
import model.security.TokenService
import model.security.validateTokenIsUserUser
import rabbit.EventNewReportData
import rest.validations.asObjectId
import rest.validations.asReportType
import rest.authHeader

/**
 * @api {post} /v1/comments/{commentId}/reports Crear denuncia
 * @apiName Crear denuncia
 * @apiGroup Comentarios
 *
 * @apiUse AuthHeader
 *
 * @apiExample {json} Body
 *  {
 *      "content": "{texto de denuncia}",
 *      "reportType": INNAPROPIATE|SPAM|OFF_TOPIC|FAKE
 * }
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
class PostReport(
    private val repository: ReportRepository,
    private val tokenService: TokenService
) {
    fun init(app: Routing) = app.apply {
        post<NewReportData>("/v1/comments/{commentId}/reports") {
            val user = this.call.authHeader.validateTokenIsUserUser(tokenService)
            val commentId = this.call.parameters["commentId"].asObjectId
            val reportType = it.reportType.toString().asReportType

            val report = it.copy(
                commentId = commentId,
                userId = user.id,
                reportType = reportType
            ).toNewReport.saveIn(repository).asReportData

            EventNewReportData(
                id = report.id,
                commentId = report.commentId,
                userId = report.userId,
                content = report.content,
                reportType = report.reportType,
            ).publishOn(
                exchange = "comments",
                queue = "new-reports"
            )

            this.call.respond(report)
        }
    }
}
