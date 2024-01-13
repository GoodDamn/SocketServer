package good.damn.filesharing.listeners

import android.util.Log
import androidx.annotation.WorkerThread
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset

interface NetworkInputListener {

    @WorkerThread
    fun input(
        data: ByteArray,
        out: OutputStream,
        onRetrievedFile: ((ByteArray)->Unit)
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
                val httpMessage = String(
                    data,
                    Charset.forName("UTF-8")
                )

                val path = httpMessage
                    .substring(
                        5,
                        httpMessage.indexOf(
                            " ",
                            5
                        )
                    )

                Log.d("NetworkInputListener:", "input: $path")

                onRetrievedFile(
                    onHttpGet(
                        httpMessage,
                        path
                    )
                )


            }
        }

        return true
    }

    @WorkerThread
    fun onListenChunkData(
        data: ByteArray,
        readBytes:Int,
        last: Int)

    @WorkerThread
    fun onGetFile(
        data: ByteArray,
        offset:Int,
        fileName: String)

    @WorkerThread
    fun onGetText(
        msg: String)

    @WorkerThread
    fun onHttpGet(
        request: String,
        path: String
    ) : ByteArray

}