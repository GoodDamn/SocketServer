package good.damn.filesharing.utils

import good.damn.filesharing.Application

class ResponseUtils {
    companion object {
        fun responseMessage(
            msg: String
        ): ByteArray {
            val data = msg
                .toByteArray(
                    Application.CHARSET_ASCII
                )

            return byteArrayOf(
                data.size.toByte()
            ) + data
        }

        fun responseMessageId(
            msg: String,
            msgId: Int = 1
        ): ByteArray {
            return ByteUtils.integer(msgId) + responseMessage(
                msg
            )
        }
    }
}