package model.report.dto

import com.google.gson.annotations.SerializedName
import model.report.Report
import model.report.ReportType
import utils.validator.*

data class ReportData(
    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("commentId")
    val commentId: String? = null,

    @SerializedName("userId")
    val userId: String? = null,

    @SerializedName("content")
    @Required
    @MinLen(10)
    @MaxLen(2048)
    val content: String? = null,

    @SerializedName("reviewed")
    val reviewed: Boolean = false,

    @SerializedName("reportType")
    val reportType: ReportType? = null
)

val Report.asReportData
    get() = ReportData(
        id = this.entity.id,
        commentId = this.entity.commentId,
        userId = this.entity.userId,
        content = this.entity.content,
        reviewed = this.entity.reviewed,
        reportType = this.entity.reportType
    )