package good.damn.filesharing.utils

class ByteUtils {

    companion object {
        private const val TAG = "ByteUtils"

        fun integer(i: Int): ByteArray {
            return byteArrayOf(
                ((i shr 24) and 0xff).toByte(),
                ((i shr 16) and 0xff).toByte(),
                ((i shr 8) and 0xff).toByte(),
                (i and 0xff).toByte()
            )
        }

    }
}