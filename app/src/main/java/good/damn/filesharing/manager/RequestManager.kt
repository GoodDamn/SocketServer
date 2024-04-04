package good.damn.filesharing.manager

import androidx.annotation.WorkerThread
import good.damn.filesharing.Application
import good.damn.filesharing.listeners.network.NetworkInputListener
import good.damn.filesharing.manager.request.HTTPResponseManager
import java.nio.charset.Charset

class RequestManager {

    private val mFunctions: HashMap<Int,((ByteArray)->ByteArray)> = HashMap()

    var delegate: NetworkInputListener? = null

    init {
        mFunctions[71] = { // GET; G - 71 ASCII
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

        mFunctions[1] = { // File
            val nameSize = it[1].toUByte()
            val fileName = String(it,
                2,
                nameSize.toInt(),
                Application.CHARSET
            )

            delegate?.onGetFile(
                it,
                nameSize.toInt()+2,
                fileName
            )

            ByteArray(0)
        }

        mFunctions[2] = { // Text
            val msgSize = it[1].toUByte() // 255 bytes
            val msg = String(it,
                2,
                msgSize.toInt(),
                Application.CHARSET
            )

            delegate?.onGetText(
                msg
            )
            ByteArray(0)
        }
    }

    @WorkerThread
    fun manage(
        data: ByteArray
    ): ByteArray {

        if (data.size < 2) {
            return ByteArray(0)
        }

        return mFunctions[data[0].toInt()]?.let {
            it(data)
        } ?: ByteArray(0)
    }
}
