package good.damn.filesharing.share_protocol.method.ssh

import good.damn.filesharing.Application
import good.damn.filesharing.share_protocol.method.ShareMethod
import good.damn.filesharing.utils.ResponseUtils
import java.io.File

class ShareMethodMakeDir
: ShareMethod(
    byteArrayOf(
        0x6D, 0x6B, 0x64 // mkd
    ),
    length = 3
) {
    companion object {
        private const val TAG = "ShareMethodMakeDir"
    }

    override fun response(
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int,
        userFolder: File
    ): ByteArray {

        if (argsCount <= 0) {
            return ResponseUtils.responseMessage(
                "No folder name for creating directory"
            )
        }

        val folderLen = request[argsPosition]
            .toInt()

        val folderPath = String(
            request,
            argsPosition + 1,
            folderLen,
            Application.CHARSET_ASCII
        )

        val path = File("$userFolder/$folderPath")

        if (path.exists()) {
            return ResponseUtils.responseMessage(
                "$folderPath already exists"
            )
        }

        if (path.mkdirs()) {
            return ResponseUtils.responseMessage(
                "Folder at $folderPath created")
        }

        return ResponseUtils.responseMessage(
            "Couldn't create a dir $folderPath"
        )

    }

}