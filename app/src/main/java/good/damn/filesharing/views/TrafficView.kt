package good.damn.filesharing.views

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.View
import good.damn.filesharing.views.renderer.TrafficRenderer

class TrafficView(
    context: Context
) : GLSurfaceView(
    context
) {

   init {
      setEGLContextClientVersion(
          3
      )

      setRenderer(
         TrafficRenderer()
      )

      renderMode = RENDERMODE_CONTINUOUSLY
   }


}