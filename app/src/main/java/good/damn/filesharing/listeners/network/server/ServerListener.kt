package good.damn.filesharing.listeners.network.server

import androidx.annotation.WorkerThread
import good.damn.filesharing.listeners.network.NetworkInputListener
import java.net.ServerSocket
import java.net.Socket

interface ServerListener : NetworkInputListener {
    @WorkerThread
    fun onCreateServer(server: ServerSocket)

    @WorkerThread
    fun onStartListen()

    @WorkerThread
    fun onDropClient(socket: Socket)

    @WorkerThread
    fun onDropServer()
}