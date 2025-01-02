package model.comment

import org.bson.BsonType
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonRepresentation
import java.util.*

class Comment(entityRoot: CommentEntity) {
    var entity: CommentEntity = entityRoot
        private set

    // Baja logica
    fun delete(): Comment {
        entity = entity.copy(
            deleted = Date()
        )
        return this
    }

    // Añadir like
    fun addLike(): Comment {
        entity = entity.copy(
            likesCount =+ 1,
            updated = Date()
        )
        return this
    }

    // Añadir dislike
    fun addDislike(): Comment {
        entity = entity.copy(
            dislikesCount =+ 1,
            updated = Date()
        )
        return this
    }
}


data class CommentEntity(
    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    val id: String? = null,
    @BsonRepresentation(BsonType.OBJECT_ID)
    val userId: String? = null,
    @BsonRepresentation(BsonType.OBJECT_ID)
    val articleId: String? = null,
    val content: String? = null,
    val stars: Int = 0,
    val likesCount: Int = 0,
    val dislikesCount: Int = 0,
    val deleted: Date? = null,
    val updated: Date = Date(),
    val created: Date = Date(),
)