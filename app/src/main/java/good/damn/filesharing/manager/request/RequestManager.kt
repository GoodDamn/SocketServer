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
import good.damn.filesharing.utils.FileUtils
import java.io.ByteArrayOutputStream

class RequestManager {

    companion object {
        private const val TAG = "RequestManager"
        private val SHARE_METHOD_HTTP_GET = ShareMethodHTTPGet()
        private val SHARE_METHOD_LIST = ShareMethodList()
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
