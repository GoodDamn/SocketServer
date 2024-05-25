package good.damn.filesharing.opengl.entities

import android.opengl.GLES30.*
import android.util.Log
import good.damn.filesharing.opengl.camera.BaseCamera
import good.damn.filesharing.opengl.maps.DisplacementMap
import good.damn.filesharing.opengl.textures.Texture
import good.damn.filesharing.utils.BufferUtils
import java.nio.Buffer
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import java.util.LinkedList
import kotlin.random.Random

class Landscape(
    private val mProgram: Int,
    displace: DisplacementMap,
    camera: BaseCamera
): Mesh(
    mProgram,
    camera
) {
    companion object {
        private const val TAG = "Landscape"
    }

    var texture = Texture(
        "textures/grass.jpg",
        mProgram
    )

    val width = 100
    val height = 100

    private val mNormalBuffer: FloatBuffer
    private val mTexCoordBuffer: FloatBuffer
    private val mPositionBuffer: FloatBuffer
    private val mIndicesBuffer: ShortBuffer

    private val mPositions = LinkedList<Float>()
    private val mTexCoords = LinkedList<Float>()
    private val mNormals = LinkedList<Float>()
    private val mIndices = LinkedList<Short>()

    init {

        val dgx = 1.0f / width
        val dgy = 1.0f / height

        var textureX: Float
        var textureY = 0f

        val time = System.currentTimeMillis()
        Log.d(TAG, "init: $time")
        for (z in 0..height) {
            textureX = 0f
            val fz = z.toFloat()
            for (x in 0..width) {
                val fx = x.toFloat()

                createVertex(
                    fz,
                    displace.getHeightRatio(
                        x,
                        z,
                        width,
                        height
                    ),
                    fx,
                    textureX,
                    textureY
                )

                textureX += dgx
            }

            textureY += dgy
        }
        Log.d(TAG, "init: DELTA_TIME: ${System.currentTimeMillis() - time}")

        var leftTop: Short
        var leftBottom: Short
        var rightTop: Short
        var rightBottom: Short

        val ww = width + 1

        for (y in 0 until height) {
            for (x in 0 until width) {
                leftTop = (x + y * ww).toShort()
                leftBottom = (leftTop + ww).toShort()

                rightTop = (leftTop + 1).toShort()
                rightBottom = (leftBottom + 1).toShort()

                mIndices.add(leftTop)
                mIndices.add(leftBottom)
                mIndices.add(rightBottom)
                mIndices.add(leftTop)
                mIndices.add(rightTop)
                mIndices.add(rightBottom)
            }
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

    fun setPositionAxis(
        index: Int,
        v: Float
    ) {
        mPositionBuffer.put(
            index, v
        )
    }

    fun displace(
        map: DisplacementMap
    ) {
        val c = mPositionBuffer.capacity()

        var i = 1

        var x: Int
        var z: Int

        while(i < c) {
            x = mPositionBuffer[i-1].toInt()
            z = mPositionBuffer[i+1].toInt()

            mPositionBuffer.put(
                i, map.getHeightRatio(
                    x,
                    z,
                    width,
                    height
                )
            )
            i += 3
        }
    }

    fun randomizeY() {
        val c = mPositionBuffer.capacity()

        var i = 1

        while(i < c) {
            mPositionBuffer.put(
                i, mPositionBuffer[i] + Random.nextFloat() * 3.7f
            )
            i += 3
        }
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