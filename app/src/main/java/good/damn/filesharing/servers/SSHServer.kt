package good.damn.filesharing.servers

import android.util.Log
import good.damn.filesharing.Application
import good.damn.filesharing.listeners.network.server.SSHServerListener
import good.damn.filesharing.services.network.request.SSHService
import good.damn.filesharing.shareProtocol.ssh.SSHAuth
import good.damn.filesharing.utils.FileUtils
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class SSHServer(
    port: Int,
    private val mBuffer: ByteArray
) : BaseServer<SSHServerListener>(
    port
), Runnable {

    companion object {
        private const val TAG = "SSHServer"
    }

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

        val remoteAddress = packet.address

        socket.close()

        val auth = SSHAuth
            .authenticate(
                mBuffer
            )

        delegate?.onAuth(
            auth?.user ?: "noUser"
        )

        if (auth == null || !FileUtils.isUserFolderExists(auth.user)) {
            val msg = "Invalid credentials"
                .toByteArray(
                    Application.CHARSET_ASCII
                )

            delegate?.onErrorAuth(
                "Invalid credentials"
            )

            val error = byteArrayOf(
                msg.size.toByte()
            ) + msg

            val sendPacket = DatagramPacket(
                error,
                error.size,
                remoteAddress,
                55555
            )

            Log.d(TAG, "listen: REMOTE_ADDRESS: $remoteAddress")
            
            val socket = DatagramSocket()

            socket.send(
                sendPacket
            )

            socket.close()
        }

        return true
    }

}