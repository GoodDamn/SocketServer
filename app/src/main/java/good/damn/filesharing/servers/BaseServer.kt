package good.damn.filesharing.servers

abstract class BaseServer<DELEGATE>(
    val port: Int
) {

    open var delegate: DELEGATE? = null

    abstract fun start()
    abstract fun stop()
}