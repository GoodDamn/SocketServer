package good.damn.filesharing.opengl.renderer

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import good.damn.filesharing.opengl.Mesh
import good.damn.filesharing.opengl.Object3D
import good.damn.filesharing.opengl.camera.RotationCamera
import good.damn.filesharing.opengl.entities.Entity
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TrafficRenderer
: GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "TrafficRenderer"
        lateinit var CAMERA: RotationCamera
    }

    private var mSumTick = 0f
    private var mCurrentMillis = 0L
    private var mPrevMillis = 0L
    private var mTick = 0f

    private var mWidth = 0
    private var mHeight = 0

    private var mShaderFragment = """
        precision mediump float;
        
        uniform sampler2D texture;
        
        varying lowp vec3 posOut;
        varying lowp vec2 texCoordOut;
        
        void main() {
            gl_FragColor = texture2D(
                texture,
                texCoordOut
            );
        }
    """.trimIndent()

    private var mShaderVertex = """
        attribute vec4 position;
        attribute vec2 texCoord;
        
        uniform mat4 projection;
        uniform mat4 model;
        uniform mat4 camera;
        
        varying lowp vec3 posOut;
        varying lowp vec2 texCoordOut;
        
        void main() {
            vec4 coord = camera * model * position;
            gl_Position = projection * coord;
            posOut = coord.xyz;
            texCoordOut = texCoord;
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
                    "objs/plane.obj"
                ),
                "textures/rock.jpg",
                mProgram
            ),
            Mesh(
                Object3D.createFromAssets(
                    "objs/box.obj"
                ),
                "textures/rock.jpg",
                mProgram
            ),
            Mesh(
                Object3D.createFromAssets(
                    "objs/sphere.obj"
                ),
                "textures/grass.jpg",
                mProgram
            )
        )

        mEntities[1].setPosition(
            0f,
            0.5f,
            0f
        )

        mEntities[2].setPosition(
            1f,
            0.5f,
            0f
        )

        glEnable(
            GL_DEPTH_TEST
        )

        mPrevMillis = System.currentTimeMillis()
    }

    override fun onSurfaceChanged(
        p0: GL10?,
        width: Int,
        height: Int
    ) {
        mWidth = width
        mHeight = height

        CAMERA = RotationCamera(
            width,
            height
        )

        CAMERA.radius = 6f
    }

    override fun onDrawFrame(p0: GL10?) {
        mCurrentMillis = System.currentTimeMillis()

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
git
        glViewport(
            0,
            0,
            mWidth,
            mHeight
        )

        mEntities.forEach {
            it.draw()
        }

        mTick = (mCurrentMillis - mPrevMillis) / 1000.0f

        CAMERA.rotateBy(
            mTick * 0.25f,
            0f
        )

        mSumTick += mTick
        mPrevMillis = mCurrentMillis
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