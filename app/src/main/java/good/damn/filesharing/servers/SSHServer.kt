package good.damn.filesharing.servers

import android.view.inspector.IntFlagMapping
import good.damn.filesharing.Application
import good.damn.filesharing.listeners.network.server.SSHServerListener
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class SSHServer(
    port: Int,
    private val mBuffer: ByteArray
) : BaseServer<SSHServerListener>(
    port
), Runnable {

    private var mThread: Thread? = null

    override fun start() {
        mThread = Thread(this)
        mThread?.start()
    }

    override fun stop() {
        mThread?.interrupt()
    }

    override fun run() {
        while(listen()){}
    }

    private fun listen(): Boolean {

        val socket = DatagramSocket(
            port
        )

        socket.reuseAddress = true

        val packet = DatagramPacket(
            mBuffer,
            mBuffer.size
        )

        socket.receive(
            packet
        )

        delegate?.onRequestCommand(
            mBuffer
        )

        socket.close()
        return true
    }

}