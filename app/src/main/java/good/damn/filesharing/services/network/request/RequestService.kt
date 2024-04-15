package good.damn.filesharing.services.network.request

import android.util.Log
import androidx.annotation.WorkerThread
import good.damn.filesharing.Application
import good.damn.filesharing.listeners.network.NetworkInputListener
import good.damn.filesharing.services.network.response.HTTPResponseService
import good.damn.filesharing.shareProtocol.method.ShareMethod
import good.damn.filesharing.shareProtocol.method.ShareMethodGetFile
import good.damn.filesharing.shareProtocol.method.ShareMethodHTTPGet
import good.damn.filesharing.shareProtocol.method.ShareMethodList
import good.damn.filesharing.utils.ByteUtils
import good.damn.filesharing.utils.FileUtils
import java.io.ByteArrayOutputStream

class RequestService {

    companion object {
        private const val TAG = "RequestManager"
        private val SHARE_METHOD_HTTP_GET = ShareMethodHTTPGet()
        private val SHARE_METHOD_LIST = ShareMethodList()
        private val SHARE_METHOD_GET_FILE = ShareMethodGetFile()
    }

    private val mFunctions: HashMap<
        ShareMethod,
        ((ByteArray)->ByteArray)
    > = HashMap()

    var delegate: NetworkInputListener? = null

    init {
        mFunctions[SHARE_METHOD_HTTP_GET] = {
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

            val response = HTTPResponseService()
            response.execute(
                path
            )
        }

        mFunctions.set(SHARE_METHOD_GET_FILE) { request ->
            val pathLength = request[2].toInt()

            val path = String(
                request,
                3,
                pathLength,
                Application.CHARSET_ASCII
            )

            val file = FileUtils.fromDoc(
                path
            ) ?: return@set ByteArray(0)

            val baos = ByteArrayOutputStream()

            baos.write(ByteUtils.integer(
                SHARE_METHOD_GET_FILE.hashCode()
            ))

            baos.write(ByteUtils.integer(
                file.size
            ))

            baos.write(
                file
            )

            val result = baos.toByteArray()
            baos.close()

            return@set result
        }

        mFunctions.set(SHARE_METHOD_LIST) {

            val files = FileUtils
                .getDocumentsFolder()
                .listFiles() ?: return@set ByteArray(0)

            val baos = ByteArrayOutputStream()

            baos.write(ByteUtils.integer(
                SHARE_METHOD_LIST.hashCode()
            ))

            baos.write(
                files.size
            )

            for (file in files) {
                val fileName = file.name.toByteArray(
                    Application.CHARSET_ASCII
                )

                baos.write(
                    fileName.size
                )

                baos.write(
                    fileName
                )
            }

            val bytes = baos.toByteArray()
            baos.close()

            return@set bytes // response id)
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
