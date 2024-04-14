package good.damn.filesharing.controllers

import android.util.Log
import good.damn.filesharing.listeners.network.server.ServerListener
import good.damn.filesharing.listeners.network.server.UDPServerListener
import java.net.DatagramPacket
import java.net.DatagramSocket

class UDPServer(
    private val mPort: Int,
    private val mBuffer: ByteArray
): BaseServer<UDPServerListener>(),
    Runnable {

    companion object {
        private const val TAG = "UDPServer"
    }

    private var mThread: Thread? = null

    override fun start() {
        mThread = Thread(this)
        mThread?.start()
    }

    override fun stop() {
        mThread?.interrupt()
    }

    override fun run() {
        while (listen()) {}
    }

    fun listen(): Boolean {

        val socket = DatagramSocket(
            mPort
        )

        socket.reuseAddress = true

        delegate?.onCreateDatagram(
            socket
        )

        val packet = DatagramPacket(
            mBuffer,
            mBuffer.size
        )

        socket.receive(
            packet
        )

        delegate?.onResponse(
            mBuffer
        )

        socket.close()

        return true
    }

}