package model.db

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import utils.env.Environment.env

class MongoStore {
    var pojoCodecRegistry: CodecRegistry =
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
    var codecRegistry: CodecRegistry =
        CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry)

    var clientSettings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(env.databaseUrl))
        .codecRegistry(codecRegistry)
        .build()

    private val client = MongoClient.create(clientSettings)
    val database = client.getDatabase(databaseName = "comments_db")

    inline fun <reified T : Any> collection(collectionName: String): MongoCollection<T> {
        return database.getCollection<T>(collectionName = collectionName)
    }
}
