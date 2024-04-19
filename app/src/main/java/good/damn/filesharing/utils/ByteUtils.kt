package good.damn.filesharing.utils

class ByteUtils {

    companion object {
        private const val TAG = "ByteUtils"

        fun short(
            buf: ByteArray,
            off: Int
        ): Int {
            return (buf[off].toInt() and 0xff shl 8) or
                (buf[off+1].toInt() and 0xff)
        }

        fun short(i: Int): ByteArray {
            return byteArrayOf(
                ((i shr 8) and 0xff).toByte(),
                (i and 0xff).toByte()
            )
        }

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