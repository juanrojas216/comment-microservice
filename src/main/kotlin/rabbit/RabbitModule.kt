package rabbit

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val rabbitModule = module {
    singleOf(::ConsumeAuthLogout)
    singleOf(::Consumers)
    singleOf(::EmitNewCommentData)
    singleOf(::EmitEditedCommentData)
    singleOf(::EmitNewReportData)
}