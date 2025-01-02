package rest

import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import utils.env.Environment
import utils.http.ErrorHandler
import java.io.File
import io.ktor.server.plugins.cors.routing.CORS
import rest.comment.*
import rest.replies.DeleteReplyId
import rest.replies.GetCommentReplies
import rest.replies.PostReply
import rest.report.*

class Routes(
    private val postComment: PostComment,
    private val getCommentId: GetCommentId,
    private val deleteCommentId: DeleteCommentId,
    private val getCommentsSearchCriteria: GetCommentsSearchCriteria,
    private val errorHandler: ErrorHandler,
    private val getArticleComments: GetArticleComments,
    private val getArticleSummary: GetArticleSummary,
    private val postLikeDislikeComment: PostLikeDislikeComment,
    //reports
    private val getCommentReports: GetCommentReports,
    private val postReport: PostReport,
    private val postReviewedReport: PostReviewedReport,
    //replies
    private val postReply: PostReply,
    private val deleteReply: DeleteReplyId,
    private val getCommentReplies: GetCommentReplies,
) {

    fun init() {
        embeddedServer(
            Netty,
            port = Environment.env.serverPort,
            module = {
                install(CORS){
                    anyHost()
                    allowMethod(HttpMethod.Options)
                    allowMethod(HttpMethod.Put)
                    allowMethod(HttpMethod.Patch)
                    allowMethod(HttpMethod.Delete)
                    allowHeader(HttpHeaders.ContentType)
                    allowHeader(HttpHeaders.Authorization)
                }
                install(ContentNegotiation) {
                    gson()
                }
                install(CallLogging)

                errorHandler.init(this)

                routing {
                    staticFiles("/", File(Environment.env.staticLocation))

                    postComment.init(this)
                    getCommentId.init(this)
                    deleteCommentId.init(this)
                    getCommentsSearchCriteria.init(this)
                    getArticleComments.init(this)
                    getArticleSummary.init(this)
                    postLikeDislikeComment.init(this)
                    //Reports
                    getCommentReports.init(this)
                    postReport.init(this)
                    postReviewedReport.init(this)
                    //Replies
                    postReply.init(this)
                    deleteReply.init(this)
                    getCommentReplies.init(this)
                }
            }
        ).start(wait = true)
    }
}
