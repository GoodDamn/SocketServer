package good.damn.filesharing.listeners.network.server

interface SSHServerListener {
    fun onRequestCommand(
        request: ByteArray
    )

    fun onResponseBuffer(): ByteArray
}