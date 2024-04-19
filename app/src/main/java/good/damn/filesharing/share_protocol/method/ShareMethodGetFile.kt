package good.damn.filesharing.share_protocol.method

import good.damn.filesharing.Application
import good.damn.filesharing.utils.ByteUtils
import good.damn.filesharing.utils.FileUtils
import good.damn.filesharing.utils.ResponseUtils
import java.io.ByteArrayOutputStream
import java.io.File

class ShareMethodGetFile
: ShareMethod(
    byteArrayOf(
        0x67, 0x66 // gf
    )
) {

    override fun response(
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int,
        userFolder: File
    ): ByteArray {
        val pathLength = request[argsPosition]
            .toInt()

        val path = String(
            request,
            argsPosition+1,
            pathLength,
            Application.CHARSET_ASCII
        )

        val file = FileUtils.fromDoc(
            path
        ) ?: return ResponseUtils.responseMessageId(
            "$path not exists"
        )

        val baos = ByteArrayOutputStream()

        baos.write(ByteUtils.integer(
            hashCode()
        ))

        baos.write(ByteUtils.integer(
            file.size
        ))

        baos.write(
            file
        )

        val result = baos.toByteArray()
        baos.close()

        return result
    }
}