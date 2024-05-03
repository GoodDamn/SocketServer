package good.damn.filesharing.views.renderer

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TrafficRenderer
: GLSurfaceView.Renderer {

    private var mWidth = 0
    private var mHeight = 0

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {

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
    }
}