package rest.validations

import utils.errors.ValidationError

val String?.asReportCriteriaValidation: String
    get() {
        if (this.isNullOrBlank()) {
            throw ValidationError("criteria" to "Is Invalid")
        }

        if (this != "all" && this != "unreviewed") {
            throw ValidationError("criteria" to "Is Invalid")
        }

        return this
    }