package rest.validations

import utils.errors.ValidationError

val String?.asCommentAction: String
    get() {
        if (this.isNullOrBlank()) {
            throw ValidationError("action" to "Is Invalid")
        }

        if (this != "like" && this != "dislike") {
            throw ValidationError("action" to "Is Invalid")
        }

        return this
    }