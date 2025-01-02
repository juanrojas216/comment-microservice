import model.commentsModule
import model.db.databaseModule
import model.replyModule
import model.reportModule
import model.securityModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import rabbit.Consumers
import rabbit.rabbitModule
import rest.Routes
import rest.commentsRoutesModule
import rest.reportsRoutesModule
import rest.repliesRoutesModule
import rest.routesModule

fun main() {
    Server().start()
}

class Server : KoinComponent {
    private val routes: Routes by inject()
    private val consumers: Consumers by inject()

    fun start() {
        startKoin {
            modules(routesModule, databaseModule, rabbitModule, commentsModule, reportModule, replyModule, commentsRoutesModule, reportsRoutesModule, repliesRoutesModule , securityModule)
        }

        consumers.init()
        routes.init()
    }
}