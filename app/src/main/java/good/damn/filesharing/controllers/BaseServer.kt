package good.damn.filesharing.controllers

abstract class BaseServer<DELEGATE> {

    open var delegate: DELEGATE? = null

    abstract fun start()
    abstract fun stop()
}