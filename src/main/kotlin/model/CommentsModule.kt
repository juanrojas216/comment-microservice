package model

import model.comment.CommentsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commentsModule = module {
    singleOf(::CommentsRepository)
}