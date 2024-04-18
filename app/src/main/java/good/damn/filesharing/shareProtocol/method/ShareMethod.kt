package good.damn.filesharing.shareProtocol.method

import android.util.Log
import good.damn.filesharing.shareProtocol.interfaces.Responsible
import java.io.File

open class ShareMethod(
    method: ByteArray,
    offset: Int = 0,
    length: Int = 2
): Responsible {
    
    companion object {
        private const val TAG = "ShareMethod"
    }

    private val mComputedHash = if (method.isNotEmpty())
            method.sum(offset, length) shl 1
            else -1

    override fun response(
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int,
        userFolder: File
    ):ByteArray {
        Log.d(TAG, "response: ")
        return ByteArray(0)
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
    offset: Int = 0,
    length: Int = size
): Int {
    var sum = 0
    for (i in offset until length) {
        sum += i
    }
    return sum
}