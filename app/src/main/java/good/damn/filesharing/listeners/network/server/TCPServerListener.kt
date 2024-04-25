package good.damn.filesharing.listeners.network.server

import java.net.Socket

interface TCPServerListener
    : ServerListener {

    fun onListen()

    fun onAcceptClient(
        socket: Socket
    )

    fun onDisconnectClient(
        socket: Socket
    )

    fun onHttpGet(
        request: String
    )

}