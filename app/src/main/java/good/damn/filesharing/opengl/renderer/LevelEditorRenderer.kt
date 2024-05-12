package good.damn.filesharing.opengl.renderer

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30.*
import good.damn.filesharing.opengl.camera.RotationCamera
import good.damn.filesharing.opengl.entities.Landscape
import good.damn.filesharing.opengl.light.DirectionalLight
import good.damn.filesharing.utils.AssetUtils
import good.damn.filesharing.utils.ShaderUtils

class LevelEditorRenderer
: GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "LevelEditorRenderer"
    }

    private val mCamera = RotationCamera()

    private var mWidth = 0
    private var mHeight = 0

    private var mProgram = 0

    private lateinit var mDirectionalLight: DirectionalLight
    private lateinit var mLandscape: Landscape

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

        mLandscape = Landscape(
            mProgram,
            mCamera
        )

        glEnable(
            GL_DEPTH_TEST
        )
    }

    override fun onSurfaceChanged(
        gl: GL10?,
        width: Int,
        height: Int
    ) {
        mWidth = width
        mHeight = height

        mCamera.setPerspective(
            width,
            height
        )

        mCamera.radius = 5f

        mCamera.setRotation(
            0f,
            0.01f
        )

        mDirectionalLight.setPosition(
            10f,
            -100f,
            0f
        )
    }

    override fun onDrawFrame(
        gl: GL10?
    ) {
        glViewport(
            0,
            0,
            mWidth,
            mHeight
        )

        glClear(GL_COLOR_BUFFER_BIT or
            GL_DEPTH_BUFFER_BIT
        )

        glClearColor(
            1.0f,
                1.0f,
            1.0f,
            1.0f
        )

        mDirectionalLight.draw()
        mLandscape.draw()
    }

    fun onTouchDown(
        x: Float,
        y: Float
    ) {

    }

    fun onTouchMove(
        x: Float,
        y: Float
    ) {

    }

    fun onTouchUp(
        x: Float,
        y: Float
    ) {

    }

}