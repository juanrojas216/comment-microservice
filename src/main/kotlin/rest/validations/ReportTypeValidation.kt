package rest.validations

import model.report.ReportType
import utils.errors.ValidationError

val String?.asReportType: ReportType
    get() {
        if (this.isNullOrBlank()) {
            throw ValidationError("reportType" to "Is Invalid")
        }

        return try {
            ReportType.valueOf(this)
        } catch (e: IllegalArgumentException) {
            throw ValidationError("reportType" to "Is Invalid")
        }
    }