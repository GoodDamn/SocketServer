package good.damn.filesharing.models

import android.util.Log
import java.io.ByteArrayOutputStream
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.nio.charset.Charset

class Server(port: Int) : Runnable {

    private val TAG = "Server"

    private var mHostPort = port

    private var mServer: ServerSocket? = null
    private var mServerListener: ServerListener? = null

    private var mResponseText = byteArrayOf(48)

    private val mCharset = Charset.forName("UTF-8")

    override fun run() {
        mServer = ServerSocket(mHostPort)
        mServer?.reuseAddress = true

        mServerListener?.onCreateServer(mServer!!)

        val buffer = ByteArray(128)

        while (listen(buffer)) {
            // Listen...
        }
    }

    fun setResponseText(
        text: String
    ) {
        mResponseText = text
            .toByteArray(mCharset)
    }

    fun create() {
        Thread(this)
            .start()
    }

    fun drop() {
        mServer?.close()
        mServerListener?.onDropServer()
    }

    fun setOnServerListener(
        listener: ServerListener?
    ) {
        mServerListener = listener
    }

    private fun listen(
        buffer: ByteArray
    ): Boolean {

        mServerListener?.onStartListen()
        try {
            val clientSocket = mServer!!.accept()
            val out = clientSocket.getOutputStream()

            val inp = clientSocket.getInputStream()
            val outArr = ByteArrayOutputStream()

            var n:Int
            while (true) {
                Log.d(TAG, "listen: READ ${inp.available()}")
                if (inp.available() < 2) {
                    break
                }

                n = inp.read(buffer)
                Log.d(TAG, "listen: $n ${buffer.contentToString()}")
                if (n == -1) {
                    break
                }

                outArr.write(buffer, 0, n)
                mServerListener?.onListenData(clientSocket, buffer)

            }

            val data = outArr.toByteArray()
            outArr.close()
            mServerListener?.onListenClient(
                clientSocket,
                data
            )

            out.write(mResponseText)
            out.flush()
            inp.close()
            mServerListener?.onDropClient(clientSocket)
            //clientSocket.close()
        } catch (e: SocketException) {
            Log.d(TAG, "listen: ${e.message}")
            return false
        }

        return true
    }

    interface ServerListener {
        fun onCreateServer(server: ServerSocket)
        fun onStartListen()
        fun onListenClient(socket: Socket, data: ByteArray)
        fun onDropClient(socket: Socket)
        fun onListenData(socket: Socket, data: ByteArray)
        fun onDropServer()
    }
}