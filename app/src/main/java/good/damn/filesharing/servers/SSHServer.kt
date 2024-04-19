package good.damn.filesharing.servers

import android.util.Log
import good.damn.filesharing.listeners.network.server.SSHServerListener
import good.damn.filesharing.services.network.request.SSHService
import good.damn.filesharing.share_protocol.ssh.SSHAuth
import good.damn.filesharing.utils.FileUtils
import good.damn.filesharing.utils.ResponseUtils
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

    override fun serverType(): String {
        return "SSH"
    }

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

            delegate?.onErrorAuth(
                "Invalid credentials"
            )

            responseToUser(
                remoteAddress,
                ResponseUtils.responseMessage(
                    "Invalid credentials"
                )
            )

            return true
        }

        val response = mService.makeResponse(
            auth,
            mBuffer
        )

        responseToUser(
            remoteAddress,
            response
        )

        return true
    }

    private fun responseToUser(
        address: InetAddress,
        data: ByteArray
    ) {

        val sendPacket = DatagramPacket(
            data,
            data.size,
            address,
            55555
        )

        Log.d(TAG, "listen: REMOTE_ADDRESS: $address")

        val socket = DatagramSocket()

        socket.send(
            sendPacket
        )

        socket.close()
    }

}