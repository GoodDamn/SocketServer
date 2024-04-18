package good.damn.filesharing.shareProtocol.method.ssh

import android.util.Log
import good.damn.filesharing.Application
import good.damn.filesharing.services.network.request.SSHService
import good.damn.filesharing.shareProtocol.method.ShareMethod
import good.damn.filesharing.utils.FileUtils
import good.damn.filesharing.utils.SSHUtils
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
            return SSHService.responseMessage(
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
            return SSHService.responseMessage(
                "$folderPath already exists"
            )
        }

        if (path.mkdirs()) {
            return SSHService.responseMessage(
                "Folder at $folderPath created")
        }

        return SSHService.responseMessage(
            "Couldn't create a dir $folderPath"
        )

    }

}