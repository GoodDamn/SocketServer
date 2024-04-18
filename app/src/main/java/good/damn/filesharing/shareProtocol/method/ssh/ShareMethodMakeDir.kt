package good.damn.filesharing.shareProtocol.method.ssh

import android.util.Log
import good.damn.filesharing.Application
import good.damn.filesharing.services.network.request.SSHService
import good.damn.filesharing.shareProtocol.method.ShareMethod

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
        argsPosition: Int
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

        Log.d(TAG, "response: folderPath:$folderPath")

        return SSHService.responseMessage(
            "Folder at $folderPath created"
        )
    }

}