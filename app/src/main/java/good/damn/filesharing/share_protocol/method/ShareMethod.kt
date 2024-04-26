package good.damn.filesharing.share_protocol.method

import android.util.Log
import good.damn.filesharing.share_protocol.interfaces.Responsible
import good.damn.filesharing.utils.ResponseUtils
import java.io.File

open class ShareMethod(
    method: ByteArray,
    offset: Int = 0,
    length: Int = method.size
): Responsible {
    
    companion object {
        private const val TAG = "ShareMethod"
    }

    private val mComputedHash = if (method.isNotEmpty())
            method.sum(method,offset, length) shl 1
            else -1

    override fun response(
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int,
        userFolder: File
    ):ByteArray {
        Log.d(TAG, "response: NO IMPLEMENTATION")
        return ResponseUtils.responseMessageId(
            "No implementation for this method"
        )
    }

    final override fun equals(
        other: Any?
    ): Boolean {
        return other.hashCode() == mComputedHash
    }

    final override fun hashCode(): Int {
        Log.d(TAG, "hashCode: $mComputedHash")
        return mComputedHash
    }
}

fun ByteArray.sum(
    inp: ByteArray,
    offset: Int = 0,
    length: Int = size
): Int {
    var sum = 0
    for (i in offset until length+offset) {
        sum += inp[i]
    }
    return sum
}