package good.damn.filesharing.manager

import android.util.Log
import androidx.annotation.WorkerThread
import good.damn.filesharing.listeners.NetworkInputListener
import good.damn.filesharing.manager.request.HTTPResponseManager
import java.nio.charset.Charset

class RequestManager {

    companion object {
        private val CHARSET = Charset.forName("UTF-8")
    }

    private val mFunctions: HashMap<Int,((ByteArray)->ByteArray)> = HashMap()

    var delegate: NetworkInputListener? = null

    init {
        mFunctions.set(71) { // GET; G - 71 ASCII
            val httpMessage = String(
                it,
                Charset.forName("UTF-8")
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
            return@set response
                .execute(
                    path
                )
        }

        mFunctions.set(1) { // File
            val nameSize = it[1].toUByte()
            val fileName = String(it,
                2,
                nameSize.toInt(),
                CHARSET
            )

            delegate?.onGetFile(
                it,
                nameSize.toInt()+2,
                fileName

            )

            return@set ByteArray(0)
        }

        mFunctions.set(2) { // Text
            val msgSize = it[1].toUByte()
            val msg = String(it,
                2,
                msgSize.toInt(),
                CHARSET
            )

            delegate?.onGetText(msg)
            return@set ByteArray(0)
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
