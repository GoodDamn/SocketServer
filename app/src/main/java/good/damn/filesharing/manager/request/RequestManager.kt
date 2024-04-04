package good.damn.filesharing.manager.request

import android.util.Log
import androidx.annotation.WorkerThread
import good.damn.filesharing.Application
import good.damn.filesharing.listeners.network.NetworkInputListener
import good.damn.filesharing.manager.response.HTTPResponseManager
import good.damn.filesharing.shareProtocol.ShareMethod
import good.damn.filesharing.shareProtocol.ShareMethodHTTPGet
import good.damn.filesharing.shareProtocol.ShareMethodList

class RequestManager {

    companion object {
        private const val TAG = "RequestManager"
    }

    private val mFunctions: HashMap<
        Int,
        ((ByteArray)->ByteArray)
    > = HashMap()

    var delegate: NetworkInputListener? = null

    init {
        mFunctions[ShareMethodHTTPGet().hashCode()] = { // GET; G - 71 ASCII
            val httpMessage = String(
                it,
                Application.CHARSET
            )

            delegate?.onHttpGet(httpMessage)

            val path = httpMessage
                .substring(
                    5,
                    httpMessage.indexOf(
                        " ",
                        5
                    )
                )

            val response = HTTPResponseManager()
            response.execute(
                path
            )
        }

        mFunctions[ShareMethodList().hashCode()] = {
            byteArrayOf(
                15,15,15,15, // response id
                1, // n - fileNames count
                2, // i - fileName length
                0x6c,0x6f // ij- fileName
            )
        }

    }

    @WorkerThread
    fun manage(
        data: ByteArray
    ): ByteArray {

        if (data.size < 2) {
            return ByteArray(0)
        }

        Log.d(TAG, "manage: ${data[0]}, ${data[1]}")

        return mFunctions[ShareMethod(data).hashCode()]?.let {
            it(data)
        } ?: ByteArray(0)
    }
}
