package good.damn.filesharing.opengl.renderer

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30.*
import good.damn.filesharing.opengl.camera.RotationCamera
import good.damn.filesharing.opengl.entities.Landscape
import good.damn.filesharing.opengl.light.DirectionalLight
import good.damn.filesharing.opengl.ui.GLButton
import good.damn.filesharing.utils.AssetUtils
import good.damn.filesharing.utils.ShaderUtils

class LevelEditorRenderer
: GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "LevelEditorRenderer"
    }

    private lateinit var mBtnZoomOut: GLButton
    private lateinit var mBtnZoomIn: GLButton
    private lateinit var mBtnRandomizeLand: GLButton

    private var isUi = false

    private val mCamera = RotationCamera()

    private var mPrevX = 0f
    private var mPrevY = 0f

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

        val btnLen = mWidth * 0.1f

        mBtnZoomOut = GLButton(
            0f,
            0f,
            btnLen,
            btnLen
        ) {
            mCamera.radius += 2
        }

        mBtnZoomIn = GLButton(
            btnLen,
            0f,
            btnLen,
            btnLen
        ) {
            mCamera.radius -= 2
        }

        mBtnRandomizeLand = GLButton(
            mWidth - btnLen,
            0f,
            btnLen,
            btnLen
        ) {
            mLandscape.randomizeY()
        }
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
            0.0f,
                0.0f,
            0.0f,
            1.0f
        )

        mDirectionalLight.draw()
        mLandscape.draw()
    }

    fun onTouchDown(
        x: Float,
        y: Float
    ) {
        if (mBtnZoomOut.intercept(x, y) ||
            mBtnZoomIn.intercept(x,y) ||
            mBtnRandomizeLand.intercept(x,y)
        ) {
            isUi = true
            return
        }

        mPrevX = x
        mPrevY = y
    }

    fun onTouchMove(
        x: Float,
        y: Float
    ) {
        if (isUi) {
            return
        }

        mCamera.rotateBy(
            (mPrevX - x) * 0.001f,
            (y - mPrevY) * 0.001f
        )

        mPrevX = x
        mPrevY = y
    }

    fun onTouchUp(
        x: Float,
        y: Float
    ) {
        isUi = false
    }

}