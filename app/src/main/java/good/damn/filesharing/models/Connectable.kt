package good.damn.filesharing.models

import android.util.Log
import java.io.ByteArrayOutputStream
import java.net.Socket
import java.nio.charset.Charset

interface Connectable {

    fun onConnected(socket: Socket)

    fun onGetResponse(data: ByteArray)

    fun connectToHost(
        ip: String,
        port: Int
    ) {
        Thread {
            val socket = Socket(ip,port)

            onConnected(socket)

            val out = socket.getOutputStream()
            val inp = socket.getInputStream()

            out.write("Hello! I'm your client!".toByteArray(
                Charset.forName("UTF-8")
            ))
            out.flush()

            val outArr = ByteArrayOutputStream()

            val buffer = ByteArray(128)
            var n:Int
            while (true) {
                Log.d("Connectable:", "connectToHost: READ ${inp.available()}")
                n = inp.read(buffer)
                if (n == -1) {
                    break
                }

                outArr.write(buffer,0,n)
            }

            val inputData = outArr.toByteArray()
            outArr.close()
            inp.close()
            out.close()
            socket.close()

            onGetResponse(inputData)
        }.start()
    }
}