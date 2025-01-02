package rabbit

class Consumers(
    private val consumeAuthLogout: ConsumeAuthLogout,
) {
    fun init() {
        consumeAuthLogout.init()
    }
}