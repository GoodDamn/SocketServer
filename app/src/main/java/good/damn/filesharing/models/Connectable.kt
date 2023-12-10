package good.damn.filesharing.models

import android.util.Log
import good.damn.filesharing.utils.NetworkUtils
import java.io.ByteArrayOutputStream
import java.net.Socket
import java.nio.charset.Charset

interface Connectable {

    fun onConnected(socket: Socket)

    fun onGetResponse(data: ByteArray)

    fun onSendBytes(): ByteArray

    fun connectToHost(
        ip: String,
        port: Int,
        buffer: ByteArray
    ) {
        Thread {
            val socket = Socket(ip,port)

            onConnected(socket)

            val out = socket.getOutputStream()
            val inp = socket.getInputStream()

            out.write(onSendBytes())
            out.flush()

            val inputData = NetworkUtils
                .readBytes(inp, buffer)

            inp.close()
            out.close()
            socket.close()

            onGetResponse(inputData)
        }.start()
    }
}