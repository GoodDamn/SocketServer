package good.damn.filesharing.servers

import good.damn.filesharing.listeners.network.server.UDPServerListener
import java.net.DatagramPacket
import java.net.DatagramSocket

class UDPServer(
    port: Int,
    private val mBuffer: ByteArray
): BaseServer<UDPServerListener>(
    port
), Runnable {

    companion object {
        private const val TAG = "UDPServer"
    }

    private var mThread: Thread? = null

    override fun serverType(): String {
        return "UDP"
    }

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

    private fun listen(): Boolean {

        val socket = DatagramSocket(
            port
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