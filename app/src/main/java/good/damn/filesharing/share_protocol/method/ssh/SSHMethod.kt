package good.damn.filesharing.share_protocol.method.ssh

import android.util.Log
import good.damn.filesharing.share_protocol.interfaces.ResponseSSH
import good.damn.filesharing.share_protocol.method.sum
import good.damn.filesharing.utils.ResponseUtils
import java.io.File

open class SSHMethod(
    method: ByteArray,
    offset: Int = 0,
    length: Int = method.size
): ResponseSSH {

    companion object {
        private const val TAG = "SSHMethod"
    }

    private val mComputedHash = if (method.isNotEmpty())
        method.sum(method,offset, length) shl 1
    else -1

    override fun response(
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int,
        userFolder: File
    ): ByteArray {
        Log.d(TAG, "response: NOT IMPLEMENTED")
        return ResponseUtils.responseMessageId(
            "Method not implemented"
        )
    }

}