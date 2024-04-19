package good.damn.filesharing.shareProtocol.method

import good.damn.filesharing.Application
import good.damn.filesharing.utils.ByteUtils
import good.damn.filesharing.utils.FileUtils
import good.damn.filesharing.utils.ResponseUtils
import java.io.ByteArrayOutputStream
import java.io.File

class ShareMethodList
: ShareMethod(
    byteArrayOf(0x6C, 0x69), // li
    length = 2
) {
    override fun response(
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int,
        userFolder: File
    ): ByteArray {

        val files = FileUtils
            .getDocumentsFolder()
            .listFiles() ?: return ResponseUtils.responseMessage(
                    "No files inside this server"
                )

        val baos = ByteArrayOutputStream()

        baos.write(
            ByteUtils.integer(
            hashCode()
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
        return bytes
    }
}