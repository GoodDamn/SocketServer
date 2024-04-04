package good.damn.filesharing.shareProtocol

import android.util.Log

open class ShareMethod(
    val method: ByteArray
) {
    companion object {
        private const val TAG = "ShareMethod"
    }

    private val mComputedHash = if (method.size >= 2)
        (method[0] + method[1]) shl 1
        else -1

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