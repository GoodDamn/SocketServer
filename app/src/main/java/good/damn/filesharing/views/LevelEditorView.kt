package good.damn.filesharing.views

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.MotionEvent
import android.view.View
import good.damn.filesharing.activities.other.opengl.LevelEditorActivity
import good.damn.filesharing.opengl.renderer.LevelEditorRenderer
import java.io.InputStream

class LevelEditorView(
    context: LevelEditorActivity
): GLSurfaceView(
    context
) {

    companion object {
        private const val TAG = "LevelEditorView"
    }

    private val mRenderer: LevelEditorRenderer

    init {
        setEGLContextClientVersion(
            3
        )

        mRenderer = LevelEditorRenderer(
            context
        )

        setRenderer(
            mRenderer
        )

        renderMode = RENDERMODE_CONTINUOUSLY
    }


    override fun onTouchEvent(
        event: MotionEvent?
    ): Boolean {
        if (event == null) {
            return false
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mRenderer.onTouchDown(
                    event.x,
                    event.y
                )
            }

            MotionEvent.ACTION_MOVE -> {
                mRenderer.onTouchMove(
                    event.x,
                    event.y
                )
            }

            MotionEvent.ACTION_UP -> {
                mRenderer.onTouchUp(
                    event.x,
                    event.y
                )
            }
        }

        return true
    }

    fun onLoadFromUserDisk(
        inp: InputStream?
    ) {
        if (inp == null) {
            return
        }

        mRenderer.onLoadFromUserDisk(
            inp
        )
    }

}