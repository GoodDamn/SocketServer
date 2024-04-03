package good.damn.filesharing.listeners.network

import android.util.Log
import androidx.annotation.WorkerThread
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset

interface NetworkInputListener {

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
        request: String
    )

}