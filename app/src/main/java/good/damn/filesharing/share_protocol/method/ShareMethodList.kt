package good.damn.filesharing.share_protocol.method

import good.damn.filesharing.Application
import good.damn.filesharing.utils.ByteUtils
import good.damn.filesharing.utils.FileUtils
import good.damn.filesharing.utils.ResponseUtils
import java.io.File
import java.io.OutputStream

class ShareMethodList
: ShareMethodStream(
    byteArrayOf(0x6C, 0x69) // li
) {
    override fun responseStream(
        out: OutputStream,
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int
    ) {

        val files = FileUtils
            .getDocumentsFolder()
            .listFiles()

        if (files == null) {
            ResponseUtils.responseMessageIdStream(
                out,
                "No files inside this server"
            )
            return
        }

        out.write(
            ByteUtils.integer(
            hashCode()
        ))

        out.write(
            files.size
        )

        for (file in files) {
            val fileName = file.name.toByteArray(
                Application.CHARSET_ASCII
            )

            out.write(
                fileName.size
            )

            out.write(
                fileName
            )
        }
    }
}