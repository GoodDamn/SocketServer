package good.damn.filesharing.shareProtocol.method.ssh

import android.util.Log
import good.damn.filesharing.services.network.request.SSHService
import good.damn.filesharing.shareProtocol.method.ShareMethod

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
        offset: Int
    ): ByteArray {
        val cmdLen = request[offset]

        Log.d(TAG, "response: $cmdLen ${request[offset + 4]}")

        if (request[offset + 4].toInt() == 0x20) { // [Space]

        }

        return ByteArray(0)
    }

}