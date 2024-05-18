package good.damn.filesharing.share_protocol.method

import good.damn.filesharing.Application
import good.damn.filesharing.utils.ByteUtils
import good.damn.filesharing.utils.FileUtils
import good.damn.filesharing.utils.ResponseUtils
import java.io.File
import java.io.OutputStream

class ShareMethodGetFile
: ShareMethodStream(
    byteArrayOf(
        0x67, 0x66 // gf
    )
) {

    override fun responseStream(
        out: OutputStream,
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int
    ) {
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
        )

        if (file == null) {
            ResponseUtils.responseMessageIdStream(
                out,
                "$path not exists"
            )
            return
        }

        out.write(ByteUtils.integer(
            hashCode()
        ))

        out.write(ByteUtils.integer(
            file.size
        ))

        out.write(
            file
        )
    }
}