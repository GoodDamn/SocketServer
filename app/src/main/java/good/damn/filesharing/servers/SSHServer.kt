package good.damn.filesharing.servers

import good.damn.filesharing.listeners.network.server.SSHServerListener
import good.damn.filesharing.services.network.request.SSHService
import java.net.DatagramPacket
import java.net.DatagramSocket

class SSHServer(
    port: Int,
    private val mBuffer: ByteArray
) : BaseServer<SSHServerListener>(
    port
), Runnable {

    private var mThread: Thread? = null

    private val mService = SSHService()

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

        val response = mService.authentication(
            mBuffer
        )

        if (response != null) {
            val sendPacket = DatagramPacket(
                response,
                response.size
            )

            socket.send(
                sendPacket
            )
        }

        socket.close()
        return true
    }

}