package good.damn.filesharing.opengl.entities

import android.opengl.GLES30.*
import android.util.FloatMath
import good.damn.filesharing.opengl.Mesh
import good.damn.filesharing.opengl.Vector
import good.damn.filesharing.utils.BufferUtils
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import java.util.LinkedList
import javax.mail.search.FlagTerm

class Landscape(
    private val mProgram: Int
) {

    private val mVertexBuffer: FloatBuffer
    private val mIndicesBuffer: ShortBuffer

    private val mVertices = LinkedList<Float>()
    private val mIndices = LinkedList<Short>()

    private val mAttrPosition: Int = glGetAttribLocation(
        mProgram,
        "position"
    )

    private val mAttrNormal: Int = glGetAttribLocation(
        mProgram,
        "normal"
    )

    private val mAttrTexCoord: Int = glGetAttribLocation(
        mProgram,
        "texCoord"
    )

    init {

        val gridX = 10
        val dgx = 1.0f / gridX

        var tx = 0.0f

        var leftTop: Short
        var leftBottom: Short
        var rightTop: Short
        var rightBottom: Short


        for (x in 0 until gridX) {

            if (x >= 1) {
                rightTop = (x * 2).toShort()
                rightBottom = (rightTop + 1).toShort()
                leftTop = (rightTop - 2).toShort()
                leftBottom = (rightTop - 1).toShort()

                mIndices.add(leftTop)
                mIndices.add(leftBottom)
                mIndices.add(rightBottom)
                mIndices.add(leftTop)
                mIndices.add(rightTop)
                mIndices.add(rightBottom)
            }

            val fx = x.toFloat()

            createVertex(
                fx,
                0.0f,
                0.0f,
                tx,
                0.0f
            )

            createVertex(
                fx,
                0.0f,
                1.0f,
                tx,
                1.0f
            )

            tx += dgx
        }

        val arrVert = mVertices.toFloatArray()

        mVertexBuffer = BufferUtils
            .createFloatBuffer(
                arrVert
            )

        val arrIndex = mIndices.toShortArray()

        mIndicesBuffer = BufferUtils
            .createShortBuffer(
                arrIndex
            )
    }

    fun draw() {

        glEnableVertexAttribArray(
            mAttrPosition
        )

        glVertexAttribPointer(
            mAttrPosition,
            3,
            GL_FLOAT,
            false,
            32,
            mVertexBuffer
        )

        glEnableVertexAttribArray(
            mAttrTexCoord
        )

        glVertexAttribPointer(
            mAttrTexCoord,
            2,
            GL_FLOAT,
            false,
            12,
            mVertexBuffer
        )

        glEnableVertexAttribArray(
            mAttrNormal
        )

        glVertexAttribPointer(
            mAttrNormal,
            3,
            GL_FLOAT,
            false,
            20,
            mVertexBuffer
        )

        glDrawElements(
            GL_TRIANGLES,
            mIndicesBuffer.capacity(),
            GL_UNSIGNED_SHORT,
            mIndicesBuffer
        )

        glDisableVertexAttribArray(
            mAttrPosition
        )

        glDisableVertexAttribArray(
            mAttrTexCoord
        )

        glDisableVertexAttribArray(
            mAttrNormal
        )
    }

    private fun createVertex(
        x: Float,
        y: Float,
        z: Float,
        tx: Float, // Texture coords
        ty: Float
    ) {
        // Position
        mVertices.add(x)
        mVertices.add(y)
        mVertices.add(z)

        // TexCoords
        mVertices.add(tx)
        mVertices.add(ty)

        // Normal
        mVertices.add(0.0f)
        mVertices.add(1.0f)
        mVertices.add(0.0f)
    }

}