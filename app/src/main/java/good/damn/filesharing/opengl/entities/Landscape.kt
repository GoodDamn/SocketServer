package good.damn.filesharing.opengl.entities

import android.opengl.GLES30.*
import good.damn.filesharing.opengl.camera.BaseCamera
import good.damn.filesharing.opengl.textures.Texture
import good.damn.filesharing.utils.BufferUtils
import java.nio.Buffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
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

    private val mNormalBuffer: FloatBuffer
    private val mTexCoordBuffer: FloatBuffer
    private val mPositionBuffer: FloatBuffer
    private val mIndicesBuffer: ShortBuffer

    private val mPositions = LinkedList<Float>()
    private val mTexCoords = LinkedList<Float>()
    private val mNormals = LinkedList<Float>()
    private val mIndices = LinkedList<Short>()

    init {

        val gridX = 2
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
                0.0f,
                0.0f,
                fx,
                tx,
                1.0f
            )

            createVertex(
                1.0f,
                0.0f,
                fx,
                tx,
                0.0f
            )

            tx += dgx
        }

        mPositionBuffer = BufferUtils
            .createFloatBuffer(
                mPositions.toFloatArray()
            )

        mNormalBuffer = BufferUtils
            .createFloatBuffer(
                mNormals.toFloatArray()
            )

        mTexCoordBuffer = BufferUtils
            .createFloatBuffer(
                mTexCoords.toFloatArray()
            )

        mIndicesBuffer = BufferUtils
            .createShortBuffer(
                mIndices.toShortArray()
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
            3,
            mPositionBuffer
        )

        enableVertex(
            mAttrTexCoord,
            2,
            mTexCoordBuffer
        )

        enableVertex(
            mAttrNormal,
            3,
            mNormalBuffer
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
        mPositions.add(x)
        mPositions.add(y)
        mPositions.add(z)

        // TexCoords
        mTexCoords.add(tx)
        mTexCoords.add(ty)

        // Normal
        mNormals.add(0.0f)
        mNormals.add(1.0f)
        mNormals.add(0.0f)
    }

    private fun enableVertex(
        attr: Int,
        size: Int,
        buffer: Buffer
    ) {
        glEnableVertexAttribArray(
            attr
        )

        glVertexAttribPointer(
            attr,
            size,
            GL_FLOAT,
            false,
            size * 4,
            buffer
        )
    }

}