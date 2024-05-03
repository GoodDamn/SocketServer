package good.damn.filesharing.views

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.View
import good.damn.filesharing.opengl.renderer.TrafficRenderer

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
         TrafficRenderer(
            context
         )
      )

      renderMode = RENDERMODE_CONTINUOUSLY
   }


}