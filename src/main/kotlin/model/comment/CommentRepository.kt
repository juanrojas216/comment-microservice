package model.comment

import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import model.db.MongoStore
import org.bson.types.ObjectId
import java.util.regex.Pattern

class CommentsRepository(
    store: MongoStore
) {
    private val collection = store.collection<CommentEntity>("comments")

    suspend fun save(comment: Comment): Comment {
        return if (comment.entity.id == null) {
            Comment(
                comment.entity.copy(
                    id = collection.insertOne(comment.entity).insertedId?.toString()
                )
            )
        } else {
            collection.replaceOne(Filters.eq("_id", ObjectId(comment.entity.id)), comment.entity)
            comment
        }
    }

    suspend fun findById(id: String): Comment? {
        return collection.find(Filters.eq("_id", ObjectId(id))).firstOrNull()?.let {
            Comment(it)
        }
    }

    suspend fun findByArticleId(articleId: String?) : List<Comment>{
        return collection
            .find(Filters.and(
                Filters.eq("deleted", null),
                Filters.eq("articleId", ObjectId(articleId)))
            )
            .limit(100)
            .toList()
            .map { Comment(it) }
    }

    suspend fun findByCriteria(criteria: String?): List<Comment> {
        val filter = ".*?${Pattern.quote(criteria)}.*?"
        return collection.find(
            Filters.and(
                Filters.eq("deleted_at", null),
                Filters.or(
                    Filters.regex("content", filter),
                )
            )
        ).limit(100).toList().map { Comment(it) }
    }
}

suspend fun Comment.saveIn(repository: CommentsRepository): Comment {
    return repository.save(this)
}