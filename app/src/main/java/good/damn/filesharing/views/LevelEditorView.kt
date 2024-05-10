package good.damn.filesharing.views

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.View
import good.damn.filesharing.opengl.renderer.LevelEditorRenderer

class LevelEditorView(
    context: Context
): GLSurfaceView(
    context
) {
    init {
        setEGLContextClientVersion(
            3
        )

        setRenderer(
            LevelEditorRenderer()
        )

        renderMode = RENDERMODE_CONTINUOUSLY
    }
}