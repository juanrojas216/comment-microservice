package utils.errors

import com.google.gson.annotations.SerializedName

data class OnlyUserError(
    val error: String = "Only available for users"
) : Exception() {

    fun json() = SerializedMessage(error)

    data class SerializedMessage(
        @SerializedName("error")
        var error: String? = null
    )
}