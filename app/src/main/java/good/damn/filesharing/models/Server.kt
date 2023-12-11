package good.damn.filesharing.models

import android.util.Log
import androidx.annotation.WorkerThread
import good.damn.filesharing.listeners.NetworkInputListener
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.nio.charset.Charset

class Server(port: Int) : Runnable {

    private val TAG = "Server"

    private var mHostPort = port

    private var mAttempts = 0
    private val maxAttempts = 100

    private var mServer: ServerSocket? = null
    private var mServerListener: ServerListener? = null

    private var mResponseType = 2 // text
    private var mResponse = byteArrayOf(48)
    private var mResponseText = byteArrayOf(48)

    private val mCharset = Charset.forName("UTF-8")

    override fun run() {
        mServer = ServerSocket(mHostPort)
        mServer?.reuseAddress = true

        mServerListener?.onCreateServer(mServer!!)

        val buffer = ByteArray(8192)

        while (listen(buffer)) {
            // Listen...
        }
    }

    fun setResponse(
        data: ByteArray,
        fileName: String
    ) {
        mResponseType = 1
        mResponse = data
        mResponseText = fileName
            .toByteArray(mCharset)
    }

    fun setResponseText(
        text: String
    ) {
        mResponseType = 2
        mResponse = byteArrayOf()
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

            mAttempts = 0

            var n: Int

            outArr.write(inp.read()) // type

            while (true) {
                Log.d(TAG, "listen: READ ${inp.available()}")
                if (inp.available() == 0) {
                    mAttempts++
                    if (mAttempts >= maxAttempts) {
                        break
                    }
                    continue
                }

                mAttempts = 0

                n = inp.read(buffer)
                Log.d(TAG, "listen: $n ${buffer.contentToString()}")
                if (n == -1) {
                    break
                }

                outArr.write(buffer, 0, n)
            }

            val data = outArr.toByteArray()
            outArr.close()

            Log.d(TAG, "listen: ${data.contentToString()}")
            
            mServerListener?.input(data)

            out.write(mResponseType)
            out.write(mResponseText.size)
            out.write(mResponseText)
            out.write(mResponse)
            out.flush()
            mServerListener?.onDropClient(clientSocket)

        } catch (e: SocketException) {
            Log.d(TAG, "listen: EXCEPTION:  ${e.message}")
            return false
        }

        return true
    }

    interface ServerListener : NetworkInputListener {
        @WorkerThread
        fun onCreateServer(server: ServerSocket)

        @WorkerThread
        fun onStartListen()

        @WorkerThread
        fun onDropClient(socket: Socket)

        @WorkerThread
        fun onDropServer()
    }
}