package good.damn.filesharing.opengl.renderer

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import good.damn.filesharing.opengl.StaticMesh
import good.damn.filesharing.opengl.Object3D
import good.damn.filesharing.opengl.camera.RotationCamera
import good.damn.filesharing.opengl.entities.Mesh
import good.damn.filesharing.opengl.light.DirectionalLight
import good.damn.filesharing.utils.AssetUtils
import good.damn.filesharing.utils.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TrafficRenderer
: GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "TrafficRenderer"
    }

    private var mSumTick = 0f
    private var mCurrentMillis = 0L
    private var mPrevMillis = 0L
    private var mTick = 0f

    private var mWidth = 0
    private var mHeight = 0

    private var mProgram = 0

    private lateinit var mEntities: Array<Mesh>
    private lateinit var mDirectionalLight: DirectionalLight
    private var mCamera = RotationCamera()

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {

        mProgram = glCreateProgram()

        glAttachShader(
            mProgram,
            ShaderUtils.createShader(
                GL_VERTEX_SHADER,
                AssetUtils.loadString(
                    "shaders/vert.glsl"
                )
            )
        )

        glAttachShader(
            mProgram,
            ShaderUtils.createShader(
                GL_FRAGMENT_SHADER,
                AssetUtils.loadString(
                    "shaders/frag.glsl"
                )
            )
        )

        glLinkProgram(
            mProgram
        )

        glUseProgram(
            mProgram
        )

        mDirectionalLight = DirectionalLight(
            mProgram
        )

        mEntities = arrayOf(
            StaticMesh(
                Object3D.createFromAssets(
                    "objs/plane.obj"
                ),
                "textures/rock.jpg",
                mProgram,
                mCamera
            ),
            StaticMesh(
                Object3D.createFromAssets(
                    "objs/box.obj"
                ),
                "textures/rock.jpg",
                mProgram,
                mCamera
            ),
            StaticMesh(
                Object3D.createFromAssets(
                    "objs/sphere.obj"
                ),
                "textures/rock.jpg",
                mProgram,
                mCamera
            ),
            StaticMesh(
                Object3D.createFromAssets(
                    "objs/walls.obj"
                ),
                "textures/grass.jpg",
                mProgram,
                mCamera
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
        mCamera.radius = 6f

        mCamera.setPerspective(
            width,
            height
        )
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

        glViewport(
            0,
            0,
            mWidth,
            mHeight
        )

        mDirectionalLight.draw()

        mEntities.forEach {
            it.draw()
        }

        mTick = (mCurrentMillis - mPrevMillis) / 1000.0f

        mCamera.rotateBy(
            mTick * 0.25f,
            0f
        )

        mSumTick += mTick
        mPrevMillis = mCurrentMillis
    }
}