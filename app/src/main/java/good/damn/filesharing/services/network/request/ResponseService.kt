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
import good.damn.filesharing.utils.ResponseUtils
import java.io.File

class ResponseService {

    companion object {
        private const val TAG = "RequestManager"
        private const val SHARE_PROTOCOL_TYPE: Byte = 0
        private val HTTP_METHOD_GET = ShareMethodHTTPGet()
        private val SHARE_METHOD_LIST = ShareMethodList()
        private val SHARE_METHOD_GET_FILE = ShareMethodGetFile()
        private val NULL_FILE = File("/")
        private val SHARE_METHODS: HashMap<
            ShareMethod,
            Responsible
            > = hashMapOf(
                SHARE_METHOD_GET_FILE to SHARE_METHOD_GET_FILE,
                SHARE_METHOD_LIST to SHARE_METHOD_LIST
            )

        private val HTTP_METHODS: HashMap<
            ShareMethod,
            Responsible
            > = hashMapOf(
            HTTP_METHOD_GET to HTTP_METHOD_GET
            )
    }

    var delegate: NetworkInputListener? = null

    @WorkerThread
    fun manage(
        data: ByteArray
    ): ByteArray {
        if (data.size < 2) {
            return ByteArray(0)
        }


        val protocolType = data[0]
        var offset = 0
        var methodLen = 3
        var methods = HTTP_METHODS
        var argsCount = 0
        var argsPosition = 0

        if (protocolType == SHARE_PROTOCOL_TYPE) {
            offset = 2
            methodLen = data[1]
                .toInt()
            methods = SHARE_METHODS

            val argsCountPos = offset+methodLen

            if (argsCountPos != data.size) {
                // Has arguments
                argsCount = data[argsCountPos]
                    .toInt()
                argsPosition = argsCountPos + 1
            }
        }

        Log.d(TAG, "manage: offset: $offset; length: $methodLen;")
        
        return methods[ShareMethod(
            data,
            offset = offset,
            length = methodLen
        )]?.response(
            data,
            argsCount,
            argsPosition,
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
