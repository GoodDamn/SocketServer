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

        fun integer(
            inp: ByteArray,
            offset: Int
        ): Int {
            return (inp[offset].toInt() and 0xff shl 24) or
                (inp[offset+1].toInt() and 0xff shl 16) or
                (inp[offset+2].toInt() and 0xff shl 8) or
                (inp[offset+3].toInt() and 0xff)
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