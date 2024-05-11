package good.damn.filesharing.opengl.entities

import android.opengl.GLES30.*
import good.damn.filesharing.opengl.camera.BaseCamera
import good.damn.filesharing.opengl.textures.Texture
import good.damn.filesharing.utils.BufferUtils
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import java.util.LinkedList

class Landscape(
    private val mProgram: Int,
    camera: BaseCamera
): Mesh(
    mProgram,
    camera
) {

    var texture = Texture(
        "textures/grass.jpg",
        mProgram
    )

    private val mVertexBuffer: FloatBuffer
    private val mIndicesBuffer: ShortBuffer

    private val mVertices = LinkedList<Float>()
    private val mIndices = LinkedList<Short>()

    init {

        val gridX = 1
        val dgx = 1.0f / gridX

        var tx = 0.0f

        var leftTop: Short
        var leftBottom: Short
        var rightTop: Short
        var rightBottom: Short


        for (x in 0..gridX) {

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
                -1.0f
            )

            createVertex(
                fx,
                0.0f,
                1.0f,
                tx,
                0.0f
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

    override fun draw() {
        super.draw()

        mAttrPosition = glGetAttribLocation(
            mProgram,
            "position"
        )

        mAttrTexCoord = glGetAttribLocation(
            mProgram,
            "texCoord"
        )

        mAttrNormal = glGetAttribLocation(
            mProgram,
            "normal"
        )

        enableVertex(
            mAttrPosition,
            3
        )

        enableVertex(
            mAttrTexCoord,
            2
        )

        enableVertex(
            mAttrNormal,
            3
        )

        glDrawElements(
            GL_TRIANGLES,
            mIndicesBuffer.capacity(),
            GL_UNSIGNED_SHORT,
            mIndicesBuffer
        )

        texture.draw()

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

    private fun enableVertex(
        attr: Int,
        size: Int
    ) {
        glEnableVertexAttribArray(
            attr
        )

        glVertexAttribPointer(
            attr,
            size,
            GL_FLOAT,
            false,
            mStride,
            mVertexBuffer
        )
    }

}