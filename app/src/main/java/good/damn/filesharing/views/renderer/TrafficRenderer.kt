package good.damn.filesharing.views.renderer

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TrafficRenderer
: GLSurfaceView.Renderer {

    private var mWidth = 0
    private var mHeight = 0

    private val mIndices: ShortArray = shortArrayOf(
        0, 1, 2, 0, 2, 3
    )

    private val mSquareCoords: FloatArray = floatArrayOf(
        -0.5f, 0.5f,  // top left
        -0.5f, -0.5f, // bottom left
        0.5f, -0.5f,  // bottom right
        0.5f, 0.5f,   // top right
    )

    private val mCOORDINATES_PER_VERTEX = 3 // number of coords
    private val mVertexOffset = mCOORDINATES_PER_VERTEX * 4 // (x,y,z) vertex per 4 bytes

    private lateinit var mVertexBuffer: FloatBuffer
    private lateinit var mIndicesBuffer: ShortBuffer

    private var mShaderFragment = """
        
        void main() {
            gl_FragColor = vec4(1.0,1.0,0.0,1.0);
        }
    """.trimIndent()

    private var mShaderVertex = """
        attribute vec4 position;
        void main() {
            gl_Position = position;
        }
    """.trimIndent()

    private var mProgram = 0

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {

        val byteBuffer =
            ByteBuffer.allocateDirect(mSquareCoords.size * 4)
        byteBuffer.order(ByteOrder.nativeOrder())
        mVertexBuffer = byteBuffer.asFloatBuffer()
        mVertexBuffer.put(mSquareCoords)
        mVertexBuffer.position(0)

        val drawByteBuffer: ByteBuffer =
            ByteBuffer.allocateDirect(mIndices.size * 2)
        drawByteBuffer.order(ByteOrder.nativeOrder())
        mIndicesBuffer = drawByteBuffer.asShortBuffer()
        mIndicesBuffer.put(mIndices)
        mIndicesBuffer.position(0)

        mProgram = glCreateProgram()

        val shaderVert = glCreateShader(
            GL_VERTEX_SHADER
        )

        glShaderSource(
            shaderVert,
            mShaderVertex
        )

        val shaderFrag = glCreateShader(
            GL_FRAGMENT_SHADER
        )

        glShaderSource(
            shaderFrag,
            mShaderFragment
        )

        glCompileShader(
            shaderVert
        )

        glCompileShader(
            shaderFrag
        )

        glAttachShader(
            mProgram,
            shaderVert
        )

        glAttachShader(
            mProgram,
            shaderFrag
        )

        glLinkProgram(
            mProgram
        )

        glUseProgram(
            mProgram
        )

        glEnable(
            GL_TRIANGLES
        )
    }

    override fun onSurfaceChanged(
        p0: GL10?,
        width: Int,
        height: Int
    ) {
        mWidth = width
        mHeight = height
    }

    override fun onDrawFrame(p0: GL10?) {
        glClear(
            GL_COLOR_BUFFER_BIT or
            GL_DEPTH_BUFFER_BIT
        )

        glClearColor(
            0f,
            1f,
            0f,
            1f
        )

        glViewport(
            0,
            0,
            mWidth,
            mHeight
        )

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