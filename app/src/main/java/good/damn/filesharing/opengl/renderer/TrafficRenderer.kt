package good.damn.filesharing.opengl.renderer

import android.content.EntityIterator
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import good.damn.filesharing.opengl.entities.Entity
import good.damn.filesharing.opengl.entities.primitives.Plane
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

    private var mShaderFragment = """
        precision mediump float;
        void main() {
            float lin = gl_FragCoord.x / 750.0;
            gl_FragColor = vec4(0.5,0.0,lin,1.0);
        }
    """.trimIndent()

    private var mShaderVertex = """
        attribute vec4 position;
        void main() {
            gl_Position = position;
        }
    """.trimIndent()

    private var mProgram = 0

    private lateinit var mEntities: Array<Entity>

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {

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

        mEntities = arrayOf(
            Plane(
                mProgram
            )
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

        mEntities.forEach {
            it.draw()
        }

    }
}