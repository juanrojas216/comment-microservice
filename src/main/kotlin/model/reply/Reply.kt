package model.reply

import org.bson.BsonType
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonRepresentation
import java.util.*

class Reply(entityRoot: ReplyEntity) {
    var entity: ReplyEntity = entityRoot
        private set

    // Baja logica
    fun delete(): Reply {
        entity = entity.copy(
            deleted = Date()
        )
        return this
    }
}


data class ReplyEntity(
    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    val id: String? = null,
    @BsonRepresentation(BsonType.OBJECT_ID)
    val userId: String? = null,
    @BsonRepresentation(BsonType.OBJECT_ID)
    val commentId: String? = null,
    val content: String? = null,
    val deleted: Date? = null,
    val created: Date = Date(),
)