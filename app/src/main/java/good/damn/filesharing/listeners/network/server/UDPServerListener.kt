package good.damn.filesharing.listeners.network.server

import androidx.annotation.WorkerThread
import java.net.DatagramSocket

interface UDPServerListener
    : ServerListener {

    @WorkerThread
    fun onCreateDatagram(
        socket: DatagramSocket
    )

    @WorkerThread
    fun onResponse(
        data: ByteArray
    )

}