package model.report

import org.bson.BsonType
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonRepresentation
import java.util.*

class Report(entityRoot: ReportEntity) {
    var entity: ReportEntity = entityRoot
        private set

    // Revisar denuncia
    fun setReviwed(): Report {
        entity = entity.copy(
            reviewed = true
        )
        return this
    }
}

data class ReportEntity(
    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    val id : String? = null,
    @BsonRepresentation(BsonType.OBJECT_ID)
    val userId : String? = null,
    @BsonRepresentation(BsonType.OBJECT_ID)
    val commentId : String? = null,
    val content: String? = null,
    val reportType: ReportType? = null,
    val reviewed: Boolean = false,
    val created: Date = Date()
)

enum class ReportType {
    INNAPROPIATE,
    SPAM,
    OFF_TOPIC,
    FAKE
}