package good.damn.filesharing.listeners.network.server

import androidx.annotation.WorkerThread
import java.net.ServerSocket
import java.net.Socket

interface ServerListener {

    @WorkerThread
    fun onCreateServer()

    @WorkerThread
    fun onDropServer()
}