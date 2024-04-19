package good.damn.filesharing.shareProtocol.method

import good.damn.filesharing.Application
import good.damn.filesharing.services.network.request.RequestService
import good.damn.filesharing.utils.ByteUtils
import good.damn.filesharing.utils.FileUtils
import java.io.ByteArrayOutputStream
import java.io.File

class ShareMethodGetFile
: ShareMethod(
    byteArrayOf(
        0x67, 0x66 // gf
    ),
    length = 2
) {

    override fun response(
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int,
        userFolder: File
    ): ByteArray {
        val pathLength = request[2].toInt()

        val path = String(
            request,
            3,
            pathLength,
            Application.CHARSET_ASCII
        )

        val file = FileUtils.fromDoc(
            path
        ) ?: return ByteArray(0)

        val baos = ByteArrayOutputStream()

        baos.write(
            ByteUtils.integer(
            hashCode()
        ))

        baos.write(
            ByteUtils.integer(
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