package good.damn.filesharing.services.network.request

import android.util.Log
import androidx.annotation.WorkerThread
import good.damn.filesharing.Application
import good.damn.filesharing.listeners.network.NetworkInputListener
import good.damn.filesharing.shareProtocol.interfaces.Responsible
import good.damn.filesharing.shareProtocol.method.ShareMethod
import good.damn.filesharing.shareProtocol.method.ShareMethodGetFile
import good.damn.filesharing.shareProtocol.method.ShareMethodHTTPGet
import good.damn.filesharing.shareProtocol.method.ShareMethodList
import good.damn.filesharing.utils.ByteUtils
import good.damn.filesharing.utils.FileUtils
import good.damn.filesharing.utils.ResponseUtils
import java.io.ByteArrayOutputStream
import java.io.File

class RequestService {

    companion object {
        private const val TAG = "RequestManager"
        private val SHARE_METHOD_HTTP_GET = ShareMethodHTTPGet()
        private val SHARE_METHOD_LIST = ShareMethodList()
        private val SHARE_METHOD_GET_FILE = ShareMethodGetFile()
        private val NULL_FILE = File("/")
    }

    private val mFunctions: HashMap<
        ShareMethod,
        Responsible
    > = hashMapOf(
        SHARE_METHOD_HTTP_GET to SHARE_METHOD_HTTP_GET,
        SHARE_METHOD_LIST to SHARE_METHOD_LIST,
        SHARE_METHOD_GET_FILE to SHARE_METHOD_GET_FILE
    )

    var delegate: NetworkInputListener? = null

    @WorkerThread
    fun manage(
        data: ByteArray
    ): ByteArray {

        if (data.size < 2) {
            return ByteArray(0)
        }

        Log.d(TAG, "manage: ${data[0]}, ${data[1]}")

        return mFunctions[ShareMethod(data)]?.response(
            data,
            1,
            2,
            NULL_FILE
        ) ?: ResponseUtils.responseMessage(
            "No such method ${String(
                data,
                0,
                2,
                Application.CHARSET_ASCII
            )}"
        )
    }
}
