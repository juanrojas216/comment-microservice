package model.comment.dto

import com.google.gson.annotations.SerializedName
import utils.validator.*

data class ArticleSummary(
    @SerializedName("average_stars")
    @Required
    val averageStars: Double = 0.0,

    @SerializedName("comment_count")
    @Required
    val commentsCount: Int = 0,
)
