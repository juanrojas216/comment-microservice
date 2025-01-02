package rabbit

import com.google.gson.annotations.SerializedName
import model.report.Report
import model.report.dto.asReportData
import model.report.ReportType
import utils.rabbit.DirectPublisher
import utils.rabbit.RabbitEvent
import utils.validator.*

class EmitNewReportData {

    companion object {

        /**
         *
         * @api {direct} comments/event-report-data Publica reportes de comentarios
         *
         * @apiGroup RabbitMQ POST
         *
         * @apiDescription Enviá de mensajes event-report-data. Publica reportes de comentarios hacia el módulo de seguridad
         *
         * @apiSuccessExample {json} Mensaje
         * {
         *      "type": "new-report-data",
         *      "message" : {
         *          "id": "{commentid}"
         *          "commentId": "{commentId}",
         *          "userId": "{userId}",
         *          "content": "{content}",
         *          "reportType": {reportType}
         *      }
         * }
         */
        fun sendReportData(exchange: String?, queue: String?, send: EventNewReportData) {
            val eventToSend = RabbitEvent(
                type = "new-report-data",
                message = send
            )
            DirectPublisher.publish(exchange, queue, eventToSend)
        }
    }
}

data class EventNewReportData(
    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("commentId")
    val commentId: String? = null,

    @SerializedName("userId")
    val userId: String? = null,

    @SerializedName("content")
    val content: String? = null,

    @SerializedName("reportType")
    val reportType: ReportType? = null
) {
    fun publishOn(exchange: String?, queue: String?) {
        EmitNewReportData.sendReportData(exchange, queue, this)
    }
}

fun Report.asEventNewReportData(referenceId: String?): EventNewReportData {
    val reportData = this.asReportData
    return EventNewReportData(
        id = reportData.id,
        commentId = reportData.commentId,
        userId = reportData.userId,
        content = reportData.content,
        reportType = reportData.reportType,
    )
}