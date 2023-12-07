package good.damn.filesharing.models

import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset

class Server(port: Int): Runnable {

    private val TAG = "Server"

    private var mHostPort = port
    private var mIsListening = false

    private var mServer: ServerSocket? = null
    private var mServerListener: ServerListener? = null

    fun create() {
        mIsListening = true
        Thread(this)
            .start()
    }

    fun drop() {
        mIsListening = false
    }

    override fun run() {
        mServer = ServerSocket(mHostPort)

        mServerListener?.onCreateServer(mServer!!)

        val buffer = ByteArray(128)

        val charset = Charset.forName("UTF-8")

        val st = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "</head>" +
                "<body> Yay! Now turn down and out)))" +
                "</body>" +
                "</html"

        while (mIsListening) {
            val clientSocket = mServer!!.accept()
            //val input = clientSocket.getInputStream()
            val out = clientSocket.getOutputStream()

            /*var n:Int

            val array = ByteArrayOutputStream()

            while (input.read(buffer).let {
                    n = it
                    it != -1}
            ) {
                array.write(buffer,0,n)
            }

            input.read(buffer)
            input.close()

            val data = array.toByteArray()
            array.close()*/

            mServerListener?.onListenClient(clientSocket, byteArrayOf())

            /*out.write(("HTTP/1.1 200 OK\n" +
                    "Date: Mon, 27 Jul 2009 12:28:53 GMT\n" +
                    "Server: Apache/2.2.14 (Win32)\n" +
                    "Last-Modified: Wed, 22 Jul 2009 19:15:56 GMT\n" +
                    "Content-Length: ${st.length}\n" +
                    "Content-Type: text/html\n" +
                    "Connection: Closed\n$st").
                toByteArray(charset))*/

            out.write(st.toByteArray(charset))
            out.close()
            clientSocket.close()
        }

        mServer?.close()
        mServerListener?.onDropServer()
    }

    fun setOnServerListener(
        listener: ServerListener?
    ) {
        mServerListener = listener
    }

    interface ServerListener {
        fun onCreateServer(server: ServerSocket)
        fun onListenClient(socket: Socket, data: ByteArray)
        fun onDropServer()
    }
}