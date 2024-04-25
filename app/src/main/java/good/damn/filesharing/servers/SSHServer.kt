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
    private val mRsaKeys: HashSet<String>,
    private val mBuffer: ByteArray
) : BaseServer<SSHServerListener>(
    port
), Runnable {

    companion object {
        private const val TAG = "SSHServer"
    }

    private var mThread: Thread? = null

    private val mService = SSHService()

    private var mServerSocket: DatagramSocket? = null

    override fun serverType(): String {
        return "SSH"
    }

    override fun start() {

        if (mThread?.isInterrupted ?: false) {
            return
        }

        mServerSocket = DatagramSocket(
            port
        )
        mThread = Thread(this)
        mThread?.start()
    }

    override fun stop() {
        if (mThread?.isInterrupted ?: true) {
            return
        }
        mThread?.interrupt()
        mServerSocket?.close()
        delegate?.onDropServer()
    }

    override fun run() {
        while(listen()){}
    }

    private fun listen(): Boolean {

        delegate?.onCreateServer()

        mServerSocket?.reuseAddress = true

        val packet = DatagramPacket(
            mBuffer,
            mBuffer.size
        )

        mServerSocket?.receive(
            packet
        )

        val remoteAddress = packet.address

        val auth = SSHAuth
            .authenticate(
                mBuffer
            )

        Log.d(TAG, "listen: AUTH: $auth")
        
        delegate?.onAuth(
            auth?.user ?: "noUser"
        )

        if (auth == null || !FileUtils.isUserFolderExists(auth.user)) {

            delegate?.onErrorAuth(
                "Invalid credentials"
            )

            responseToUser(
                remoteAddress,
                ResponseUtils.responseMessageId(
                    "Invalid credentials"
                )
            )

            return true
        }

        val hasRsa = FileUtils.hasUserRsa(auth.user)
        Log.d(TAG, "listen: HAS_RSA: $hasRsa")

        if (hasRsa) {
            if (!mRsaKeys.contains(auth.rsaKey)) {
                delegate?.onErrorAuth(
                    "Invalid RSA KEY"
                )

                responseToUser(
                    remoteAddress,
                    ResponseUtils.responseMessageId(
                        "Invalid RSA Key"
                    )
                )
                return true
            }

        } else if (auth.rsaKey != null) {
            Log.d(TAG, "listen: SAVING_RSA_KEY")
            // Save RSA key to file
            mRsaKeys.add(
                auth.rsaKey
            )

            FileUtils.saveUserRsa(
                auth.user,
                auth.rsaKey
            )
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