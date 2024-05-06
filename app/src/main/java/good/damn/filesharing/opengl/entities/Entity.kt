package good.damn.filesharing.opengl.entities

import android.opengl.GLES30.*
import android.opengl.Matrix
import good.damn.filesharing.Application
import good.damn.filesharing.opengl.renderer.TrafficRenderer
import java.nio.ByteBuffer

open class Entity(
    vertices: FloatArray,
    indices: ShortArray,
    program: Int
) {

    private val mAttrPosition: Int
    private val mAttrTexCoord: Int
    //private val mAttrNormal: Int

    private val mUniformModelView: Int
    private val mUniformProject: Int
    private val mUniformCamera: Int

    private val model = FloatArray(16)

    private val mIndicesCount = indices.size

    private var mStride = 5 * 4

    private val mVertexArray = intArrayOf(
        1
    )

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

        val bufVert = vertByte
            .asFloatBuffer()

        val bufIndex = indexByte
            .asShortBuffer()

        bufVert.put(
            vertices
        )
        bufVert.position(
            0
        )

        bufIndex.put(
            indices
        )
        bufIndex.position(
            0
        )

        mUniformModelView = glGetUniformLocation(
            program,
            "model"
        )

        mUniformProject = glGetUniformLocation(
            program,
            "projection"
        )

        mUniformCamera = glGetUniformLocation(
            program,
            "camera"
        )

        glGenVertexArrays(
            1,
            mVertexArray,
            0
        )

        glBindVertexArray(
            mVertexArray[0]
        )

        // Generate vertex buffer

        val idVertex = intArrayOf(
            1
        )

        glGenBuffers(
            1,
            idVertex,
            0
        )

        glBindBuffer(
            GL_ARRAY_BUFFER,
            idVertex[0]
        )

        glBufferData(
            GL_ARRAY_BUFFER,
            vertices.size * 4,
            bufVert,
            GL_STATIC_DRAW
        )

        mAttrTexCoord = glGetAttribLocation(
            program,
            "texCoord"
        )

        /*mAttrNormal = glGetAttribLocation(
            program,
            "normalIn"
        )*/

        mAttrPosition = glGetAttribLocation(
            program,
            "position"
        )

        // Generate index buffer

        val idIndex = intArrayOf(
            1
        )

        glGenBuffers(
            1,
            idIndex,
            0
        )

        glBindBuffer(
            GL_ELEMENT_ARRAY_BUFFER,
            idIndex[0]
        )

        glBufferData(
            GL_ELEMENT_ARRAY_BUFFER,
            indices.size * 2,
            bufIndex,
            GL_STATIC_DRAW
        )

        // Enable vertex attrib

        enableVertex(
            mAttrPosition,
            0,
            3
        )

        enableVertex(
            mAttrTexCoord,
                3 * 4,
            2
        )

        /*enableVertex(
            mAttrNormal,
            5 * 4,
            3
        )*/

        glBindVertexArray(
            0
        )

        glBindBuffer(
            GL_ARRAY_BUFFER,
            0
        )

        glBindBuffer(
            GL_ELEMENT_ARRAY_BUFFER,
            0
        )

        Matrix.setIdentityM(
            model,
            0
        )
    }

    open fun draw() {
        TrafficRenderer
            .CAMERA.draw(
                mUniformProject,
                mUniformModelView,
                mUniformCamera,
                model
            )

        glBindVertexArray(
            mVertexArray[0]
        )

        glDrawElements(
            GL_TRIANGLES,
            mIndicesCount,
            GL_UNSIGNED_SHORT,
            0
        )

        glBindVertexArray(
            0
        )
    }


    private fun enableVertex(
        attrib: Int,
        offset: Int,
        size: Int
    ) {

        glEnableVertexAttribArray(
            attrib
        )

        glVertexAttribPointer(
            attrib,
            size,
            GL_FLOAT,
            false,
            mStride,
            offset
        )

    }

}