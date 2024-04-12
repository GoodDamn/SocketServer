package good.damn.filesharing.controllers

import android.util.Log
import good.damn.filesharing.listeners.network.server.ServerListener
import good.damn.filesharing.listeners.network.server.UDPServerListener
import java.net.DatagramPacket
import java.net.DatagramSocket

class UDPServer(
    val port: Int,
    val buffer: ByteArray
): Runnable {

    companion object {
        private const val TAG = "UDPServer"
    }

    var delegate: UDPServerListener? = null

    private var mThread: Thread? = null

    fun start() {
        mThread = Thread(this)
        mThread?.start()
    }

    fun stop() {
        mThread?.interrupt()
    }

    override fun run() {
        while (listen()) {}
    }

    fun listen(): Boolean {

        val socket = DatagramSocket(
            port
        )

        socket.reuseAddress = true

        delegate?.onCreateDatagram(
            socket
        )

        val packet = DatagramPacket(
            buffer,
            buffer.size
        )

        socket.receive(
            packet
        )

        delegate?.onResponse(
            buffer
        )

        socket.close()

        return true
    }

}