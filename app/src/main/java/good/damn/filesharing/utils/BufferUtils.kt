package good.damn.filesharing.utils

import good.damn.filesharing.Application
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class BufferUtils {
    companion object {
        fun createFloatBuffer(
            i: FloatArray
        ): FloatBuffer {
            val b = ByteBuffer
                .allocateDirect(
                    i.size * 4
                ).order(
                    Application.BYTE_ORDER
                ).asFloatBuffer()
                .put(i)
            b.position(0)
            return b
        }

        fun createShortBuffer(
            i: ShortArray
        ): ShortBuffer {
            val b = ByteBuffer
                .allocateDirect(
                    i.size * 2
                ).order(
                    Application.BYTE_ORDER
                ).asShortBuffer()
                .put(i)
            b.position(0)
            return b
        }

    }
}