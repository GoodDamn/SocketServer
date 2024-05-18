package good.damn.filesharing.utils

import good.damn.filesharing.Application
import java.io.OutputStream

class ResponseUtils {
    companion object {

        fun responseMessage16IdStream(
            out: OutputStream,
            msg: String,
            msgId: Int = 2
        ) {

            val data = msg
                .toByteArray(
                    Application.CHARSET_ASCII
                )
            out.write(ByteUtils
                .integer(msgId))

            out.write(ByteUtils
                .short(data.size))

            out.write(data)
        }

        fun responseMessageIdStream(
            out: OutputStream,
            msg: String,
            msgId: Int = 1
        ) {
            val data = msg
                .toByteArray(
                    Application.CHARSET_ASCII
                )
            out.write(ByteUtils.integer(
                msgId
            ))
            out.write(data.size)
            out.write(data)
        }

        fun responseMessage16Id(
            msg: String,
            msgId: Int = 2
        ): ByteArray {
            val data = msg
                .toByteArray(
                    Application.CHARSET_ASCII
                )
            return ByteUtils.integer(msgId) + ByteUtils
                .short(data.size) + data
        }

        fun responseMessageId(
            msg: String,
            msgId: Int = 1
        ): ByteArray {
            val data = msg
                .toByteArray(
                    Application.CHARSET_ASCII
                )
            return ByteUtils.integer(msgId) + byteArrayOf(
                data.size.toByte()
            ) + data
        }
    }
}