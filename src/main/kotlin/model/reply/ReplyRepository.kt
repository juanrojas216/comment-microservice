package model.reply

import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import model.db.MongoStore
import org.bson.types.ObjectId

class ReplyRepository(
    store: MongoStore
) {
    private val collection = store.collection<ReplyEntity>("comments")

    suspend fun save(comment: Reply): Reply {
        return if (comment.entity.id == null) {
            Reply(
                comment.entity.copy(
                    id = collection.insertOne(comment.entity).insertedId?.toString()
                )
            )
        } else {
            collection.replaceOne(Filters.eq("_id", ObjectId(comment.entity.id)), comment.entity)
            comment
        }
    }

    suspend fun findById(id: String): Reply? {
        return collection.find(Filters.eq("_id", ObjectId(id))).firstOrNull()?.let {
            Reply(it)
        }
    }

    suspend fun findByCommentId(comment: String?) : List<Reply>{
        return collection
            .find(Filters.and(
                Filters.eq("deleted", null),
                Filters.eq("commentId", ObjectId(comment)))
            )
            .limit(100)
            .toList()
            .map { Reply(it) }
    }
}

suspend fun Reply.saveIn(repository: ReplyRepository): Reply {
    return repository.save(this)
}