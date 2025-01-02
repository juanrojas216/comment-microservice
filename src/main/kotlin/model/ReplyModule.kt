package model

import model.reply.ReplyRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val replyModule = module {
    singleOf(::ReplyRepository)
}