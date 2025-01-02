package model.report.dto

import com.google.gson.annotations.SerializedName
import model.report.Report
import model.report.ReportEntity
import model.report.ReportType
import utils.validator.*

data class NewReportData(
    @SerializedName("articleId")
    val commentId: String? = null,

    @SerializedName("userId")
    val userId: String? = null,

    @SerializedName("content")
    @Required
    @MinLen(10)
    @MaxLen(2048)
    val content: String? = null,

    @SerializedName("reportType")
    @Required
    val reportType: ReportType? = null
)

val NewReportData.toNewReport: Report
    get() {
        this.validate;

        return Report(
            ReportEntity(
                commentId = this.commentId,
                userId = this.userId,
                content = this.content,
                reportType = this.reportType
            )
        )
    }
