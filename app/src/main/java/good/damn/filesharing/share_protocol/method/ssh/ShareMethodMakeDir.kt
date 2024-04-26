package good.damn.filesharing.share_protocol.method.ssh

import android.util.Log
import good.damn.filesharing.Application
import good.damn.filesharing.share_protocol.method.ShareMethod
import good.damn.filesharing.utils.ResponseUtils
import java.io.File

class ShareMethodMakeDir
: ShareMethod(
    byteArrayOf(
        0x6D, 0x6B, 0x64 // mkd
    )
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
            return ResponseUtils.responseMessageId(
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
            return ResponseUtils.responseMessage16Id(
                "$folderPath already exists"
            )
        }

        if (path.mkdirs()) {
            return ResponseUtils.responseMessageId(
                "Folder at $folderPath created")
        }

        return ResponseUtils.responseMessageId(
            "Couldn't create a dir $folderPath"
        )

    }

}