package good.damn.filesharing.listeners

import androidx.annotation.WorkerThread
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset

interface NetworkInputListener {

    @WorkerThread
    fun input(
        data: ByteArray,
        out: OutputStream
    ): Boolean {
        if (data.size < 2) {
            return false
        }

        val charset = Charset
            .forName("UTF-8")

        when (data[0].toInt()) {
            1 -> { // F (File)
                val nameSize = data[1].toUByte()
                val fileName = String(data,
                    2,
                    nameSize.toInt(),
                    charset)

                onGetFile(
                    data,
                    nameSize.toInt()+2,
                    fileName)
            }
            2 -> { // T (Text)
                val msgSize = data[1].toUByte()
                val msg = String(data,
                    2,
                    msgSize.toInt(),
                    charset)
                onGetText(msg)
            }

            71 -> { // G (GET) http
                onHttpGet(out)
            }
        }

        return true
    }

    @WorkerThread
    fun onListenChunkData(data: ByteArray)

    @WorkerThread
    fun onGetFile(data: ByteArray,offset:Int,fileName: String)

    @WorkerThread
    fun onGetText(msg: String)

    @WorkerThread
    fun onHttpGet(out: OutputStream)

}