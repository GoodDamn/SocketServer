package good.damn.filesharing.shareProtocol.method.ssh

import android.util.Log
import good.damn.filesharing.Application
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

        val cmd = String(
            request,
            offset,
            cmdLen.toInt(),
            Application.CHARSET_ASCII
        )

        return ByteArray(0)
    }

}