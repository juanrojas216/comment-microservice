package model.report

import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import model.db.MongoStore
import model.report.Report
import org.bson.types.ObjectId
import java.util.regex.Pattern

class ReportRepository(
    store: MongoStore
) {
    private val collection = store.collection<ReportEntity>("reports")

    suspend fun save(report: Report): Report {
        return if (report.entity.id == null) {
            Report(
                report.entity.copy(
                    id = collection.insertOne(report.entity).insertedId?.toString()
                )
            )
        } else {
            collection.replaceOne(Filters.eq("_id", ObjectId(report.entity.id)), report.entity)
            report
        }
    }

    suspend fun findById(id: String): Report? {
        return collection.find(Filters.eq("_id", ObjectId(id))).firstOrNull()?.let {
            Report(it)
        }
    }

    suspend fun findByCommentIdUnreviewed(commentId: String?) : List<Report>{
        return collection
            .find(Filters.and(
                Filters.eq("reviewed", false),
                Filters.eq("commentId", ObjectId(commentId)))
            )
            .limit(100)
            .toList()
            .map { Report(it) }
    }

    suspend fun findByCommentId(commentId: String?) : List<Report>{
        return collection
            .find(Filters.eq("commentId", ObjectId(commentId)))
            .limit(100)
            .toList()
            .map { Report(it) }
    }
}

suspend fun Report.saveIn(repository: ReportRepository): Report {
    return repository.save(this)
}