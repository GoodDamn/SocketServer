package good.damn.filesharing

import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.InputStream

class ByteUtils {

    companion object {
        val TAG = "ByteUtils"

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