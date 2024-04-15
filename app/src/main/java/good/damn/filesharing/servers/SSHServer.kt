package good.damn.filesharing.servers

import good.damn.filesharing.Application
import good.damn.filesharing.listeners.network.server.SSHServerListener
import good.damn.filesharing.services.network.request.SSHService
import good.damn.filesharing.shareProtocol.ssh.SSHAuth
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

        val auth = SSHAuth
            .authenticate(
                mBuffer
            )

        if (auth == null) {
            val msg = "Invalid credentials"
                .toByteArray(
                    Application.CHARSET_ASCII
                )

            val error = byteArrayOf(
                msg.size.toByte()
            ) + msg

            val sendPacket = DatagramPacket(
                error,
                error.size
            )

            socket.send(
                sendPacket
            )
        }

        socket.close()
        return true
    }

}