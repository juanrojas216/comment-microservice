package rest.validations

import org.bson.types.ObjectId
import utils.errors.ValidationError

val String?.asObjectId: String
    get() {
        if (this.isNullOrBlank()) {
            throw ValidationError("id" to "Is Invalid")
        }

        try {
            ObjectId(this)
        } catch (e: Exception) {
            throw ValidationError("id" to "Is Invalid")
        }

        return this
    }