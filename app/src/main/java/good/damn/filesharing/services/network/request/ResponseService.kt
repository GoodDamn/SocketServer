package good.damn.filesharing.services.network.request

import android.util.Log
import androidx.annotation.WorkerThread
import good.damn.filesharing.Application
import good.damn.filesharing.listeners.network.server.TCPServerListener
import good.damn.filesharing.share_protocol.interfaces.ResponseStreamable
import good.damn.filesharing.share_protocol.method.*
import good.damn.filesharing.utils.ResponseUtils
import java.io.File
import java.io.OutputStream

class ResponseService {
    companion object {
        private const val TAG = "RequestManager"
        private const val SHARE_PROTOCOL_TYPE: Byte = 0
        private val HTTP_METHOD_GET = ShareMethodHTTPGet()
        private val SM_LIST = ShareMethodList()
        private val SM_GET_FILE = ShareMethodGetFile()
        private val SM_SET_FILE = ShareMethodSetFile()
        private val SM_POWER_OFF = ShareMethodPowerOff()
        private val SM_SYSTEM_INFO = ShareMethodSystemInfo()
        private val SM_SMTP = ShareMethodSMTP()
        private val SHARE_METHODS: HashMap<
            ShareMethodStream,
            ResponseStreamable
            > = hashMapOf(
                SM_GET_FILE to SM_GET_FILE,
                SM_LIST to SM_LIST,
                SM_POWER_OFF to SM_POWER_OFF,
                SM_SYSTEM_INFO to SM_SYSTEM_INFO,
                SM_SMTP to SM_SMTP,
                SM_SET_FILE to SM_SET_FILE
            )

        private val HTTP_METHODS: HashMap<
            ShareMethodStream,
            ResponseStreamable
            > = hashMapOf(
                HTTP_METHOD_GET to HTTP_METHOD_GET
            )
    }

    var delegate: TCPServerListener? = null

    @WorkerThread
    fun responseStream(
        out: OutputStream,
        inputData: ByteArray
    ) {
        if (inputData.size < 2) {
            ResponseUtils.responseMessageIdStream(
                out,
                "Null data"
            )
            return
        }

        val protocolType = inputData[0]
        var offset = 0
        var methodLen = 3
        var methods = HTTP_METHODS
        var argsCount = 0
        var argsPosition = 0

        if (protocolType == SHARE_PROTOCOL_TYPE) {
            offset = 2
            methodLen = inputData[1]
                .toInt()
            methods = SHARE_METHODS

            val argsCountPos = offset+methodLen

            if (argsCountPos != inputData.size) {
                // Has arguments
                argsCount = inputData[argsCountPos]
                    .toInt()
                argsPosition = argsCountPos + 1
            }
        }

        Log.d(TAG, "manage: offset: $offset; length: $methodLen;")
        
        methods[ShareMethodStream(
            inputData,
            offset = offset,
            length = methodLen
        )]?.responseStream(
            out,
            inputData,
            argsCount,
            argsPosition
        ) ?: ResponseUtils.responseMessageIdStream(
            out,
            "No such method ${String(
                inputData,
                offset,
                methodLen,
                Application.CHARSET_ASCII
            )}"
        )
    }
}
