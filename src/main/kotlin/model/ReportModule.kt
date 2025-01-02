package model

import model.report.ReportRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val reportModule = module {
    singleOf(::ReportRepository)
}