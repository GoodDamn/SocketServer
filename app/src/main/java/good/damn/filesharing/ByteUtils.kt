package good.damn.filesharing

class ByteUtils {

    companion object {

        fun integer(i: Int): UByteArray {
            return ubyteArrayOf(
                ((i shr 24) and 0xff).toUByte(),
                ((i shr 16) and 0xff).toUByte(),
                ((i shr 8) and 0xff).toUByte(),
                (i and 0xff).toUByte()
            )
        }

    }
}