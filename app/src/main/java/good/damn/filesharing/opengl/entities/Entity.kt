package good.damn.filesharing.opengl.entities

import android.opengl.GLES30.*
import good.damn.filesharing.Application
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.ShortBuffer

open class Entity(
    vertices: FloatArray,
    indices: ShortArray,
    private val mProgram: Int
) {

    final internal val mVertexBuffer: FloatBuffer
    final internal val mIndicesBuffer: ShortBuffer

    init {
        val vertByte = ByteBuffer
            .allocateDirect(
                vertices.size * 4
            )

        val indexByte = ByteBuffer
            .allocateDirect(
                indices.size * 2
            )

        vertByte.order(
            Application.BYTE_ORDER
        )

        indexByte.order(
            Application.BYTE_ORDER
        )

        mVertexBuffer = vertByte
            .asFloatBuffer()

        mIndicesBuffer = indexByte
            .asShortBuffer()

        mVertexBuffer.put(
            vertices
        )
        mVertexBuffer.position(
            0
        )

        mIndicesBuffer.put(
            indices
        )
        mIndicesBuffer.position(
            0
        )

    }

    final fun draw() {
        val pos = glGetAttribLocation(
            mProgram,
            "position"
        )

        glEnableVertexAttribArray(
            pos
        )

        glVertexAttribPointer(
            pos,
            2,
            GL_FLOAT,
            false,
            8,
            mVertexBuffer
        )

        glDrawElements(
            GL_TRIANGLES,
            mIndicesBuffer.capacity(),
            GL_UNSIGNED_SHORT,
            mIndicesBuffer
        )

        glDisableVertexAttribArray(
            pos
        )
    }
}