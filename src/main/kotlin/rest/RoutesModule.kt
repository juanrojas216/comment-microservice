package rest

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import rest.comment.*
import rest.report.*
import rest.replies.*
import utils.http.ErrorHandler

val commentsRoutesModule = module {
    singleOf(::PostComment)
    singleOf(::GetCommentId)
    singleOf(::DeleteCommentId)
    singleOf(::GetCommentsSearchCriteria)
    singleOf(::GetArticleComments)
    singleOf(::GetArticleSummary)
    singleOf(::PostLikeDislikeComment)
}

val reportsRoutesModule = module {
    singleOf(::GetCommentReports)
    singleOf(::PostReport)
    singleOf(::PostReviewedReport)
}

val repliesRoutesModule = module {
    singleOf(::PostReply)
    singleOf(::GetCommentReplies)
    singleOf(::DeleteReplyId)
}

val routesModule = module {
    singleOf(::ErrorHandler)
    singleOf(::Routes)
}