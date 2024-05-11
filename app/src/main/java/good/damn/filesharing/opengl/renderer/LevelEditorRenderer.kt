package good.damn.filesharing.opengl.renderer

import android.opengl.GLSurfaceView
import good.damn.filesharing.opengl.camera.BaseCamera
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30.*

class LevelEditorRenderer
: GLSurfaceView.Renderer {

    private val mCamera = BaseCamera()

    private var mWidth = 0
    private var mHeight = 0

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {

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
                0.0f,
            1.0f,
            1.0f
        )
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