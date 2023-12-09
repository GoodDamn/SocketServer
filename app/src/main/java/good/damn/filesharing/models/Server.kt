package good.damn.filesharing.models

import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.nio.charset.Charset
import java.util.*

class Server(port: Int) : Runnable {

    private val TAG = "Server"

    private var mHostPort = port

    private var mServer: ServerSocket? = null
    private var mServerListener: ServerListener? = null

    fun create() {
        Thread(this)
            .start()
    }

    fun drop() {
        mServer?.close()
        mServerListener?.onDropServer()
    }

    override fun run() {

        mServer = ServerSocket(mHostPort)
        mServer?.reuseAddress = true

        mServerListener?.onCreateServer(mServer!!)

        val buffer = ByteArray(128)

        val charset = Charset.forName("UTF-8")
        val st = ("<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "</head>" +
                "<body> Yay! Now turn down and out)))" +
                "</body>" +
                "</html")
            .toByteArray(charset)

        while (listen(buffer, st)) {
            // Listen...
        }
    }

    fun setOnServerListener(
        listener: ServerListener?
    ) {
        mServerListener = listener
    }

    private fun listen(
        buffer: ByteArray,
        resp: ByteArray
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
                if (inp.available() == 0) {
                    break
                }

                n = inp.read(buffer)
                Log.d(TAG, "listen: $n ${buffer.contentToString()}")
                if (n == -1) {
                    break
                }
                outArr.write(buffer, 0, n)
                mServerListener?.onListenData(clientSocket, buffer)

                if (buffer[n - 1] == 0.toByte()
                    || buffer[n - 1] == 48.toByte()
                ) {
                    break
                }
            }

            val data = outArr.toByteArray()
            outArr.close()
            mServerListener?.onListenClient(
                clientSocket,
                data
            )

            out.write(resp)
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