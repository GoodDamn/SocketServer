package good.damn.filesharing.opengl.renderer

import android.content.Context
import android.content.EntityIterator
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import good.damn.filesharing.opengl.Mesh
import good.damn.filesharing.opengl.Object3D
import good.damn.filesharing.opengl.entities.Entity
import good.damn.filesharing.opengl.entities.primitives.Plane
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TrafficRenderer(
    private val mContext: Context
): GLSurfaceView.Renderer {

    private var mWidth = 0
    private var mHeight = 0

    private var mShaderFragment = """
        precision mediump float;
        varying lowp vec4 posIn;
        void main() {
            gl_FragColor = vec4(
                posIn.x,
                posIn.y,
                posIn.z,
                1.0);
        }
    """.trimIndent()

    private var mShaderVertex = """
        attribute vec4 position;
        
        varying lowp vec4 posIn;
        
        void main() {
            gl_Position = position;
            posIn = position;
        }
    """.trimIndent()

    private var mProgram = 0

    private lateinit var mEntities: Array<Entity>

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {

        mProgram = glCreateProgram()

        glAttachShader(
            mProgram,
            createShader(
                GL_VERTEX_SHADER,
                mShaderVertex
            )
        )

        glAttachShader(
            mProgram,
            createShader(
                GL_FRAGMENT_SHADER,
                mShaderFragment
            )
        )

        glLinkProgram(
            mProgram
        )

        glUseProgram(
            mProgram
        )

        mEntities = arrayOf(
            Mesh(
                Object3D.createFromAssets(
                    "objs/Box.obj",
                    mContext
                ),
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
            0.1f,
            0.1f,
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

    private fun createShader(
        type: Int,
        source: String
    ): Int {
        val shader = glCreateShader(
            type
        )

        glShaderSource(
            shader,
            source
        )

        glCompileShader(
            shader
        )

        return shader
    }

}