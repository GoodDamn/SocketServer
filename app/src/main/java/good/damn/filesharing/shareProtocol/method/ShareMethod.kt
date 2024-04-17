package good.damn.filesharing.shareProtocol.method

import android.util.Log
import good.damn.filesharing.shareProtocol.interfaces.Responsible

open class ShareMethod(
    method: ByteArray,
    offset: Int = 0
): Responsible {
    companion object {
        private const val TAG = "ShareMethod"
    }

    private val mComputedHash = if (method.size - offset >= 3)
        (method[offset] + method[offset+1] + method[offset+2]) shl 1
        else -1

    override fun response(
        request: ByteArray,
        offset: Int
    ):ByteArray {return ByteArray(0)}

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