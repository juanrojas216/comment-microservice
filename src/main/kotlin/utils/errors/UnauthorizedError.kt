package utils.errors

import com.google.gson.annotations.SerializedName

data class UnauthorizedError(
    val error: String = "Unauthorized"
) : Exception() {

    fun json() = SerializedMessage(error)

    data class SerializedMessage(
        @SerializedName("error")
        var error: String? = null
    )
}