package good.damn.filesharing.manager.request

import android.util.Log
import androidx.annotation.WorkerThread
import good.damn.filesharing.Application
import good.damn.filesharing.listeners.network.NetworkInputListener
import good.damn.filesharing.manager.response.HTTPResponseManager
import good.damn.filesharing.shareProtocol.ShareMethod
import good.damn.filesharing.shareProtocol.ShareMethodHTTPGet
import good.damn.filesharing.shareProtocol.ShareMethodList
import good.damn.filesharing.utils.ByteUtils

class RequestManager {

    companion object {
        private const val TAG = "RequestManager"
        private val SHARE_METHOD_HTTP_GET = ShareMethodHTTPGet()
        private val SHARE_METHOD_LIST= ShareMethodList()
    }

    private val mFunctions: HashMap<
        ShareMethod,
        ((ByteArray)->ByteArray)
    > = HashMap()

    var delegate: NetworkInputListener? = null

    init {
        mFunctions[SHARE_METHOD_HTTP_GET] = { // GET; G - 71 ASCII
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

        mFunctions[SHARE_METHOD_LIST] = {
            ByteUtils.integer(SHARE_METHOD_LIST.hashCode()).plus( // response id
                byteArrayOf(
                    1, // n - fileNames count
                    2, // i - fileName length
                    0x6c,0x6f // fileName with 'i' length
            ))
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

        return mFunctions[ShareMethod(data)]?.let {
            it(data)
        } ?: ByteArray(0)
    }
}
