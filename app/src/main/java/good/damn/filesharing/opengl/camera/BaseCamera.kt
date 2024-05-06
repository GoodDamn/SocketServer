package good.damn.filesharing.opengl.camera

import android.opengl.GLES30.*
import android.opengl.Matrix

class BaseCamera(
    width: Int,
    height: Int
) {

    private var mProjection = FloatArray(
        16
    )

    private var model = FloatArray(
        16
    )

    init {
        Matrix.setIdentityM(
            model,
            0
        )

        Matrix.translateM(
            model,
            0,
            0f,
            0f,
            -3f
        )

        Matrix.rotateM(
            model,
            0,
            35f,
            0f,
            1f,
            1f
        )

        Matrix.perspectiveM(
            mProjection,
            0,
            85.0f,
            width.toFloat() / height.toFloat(),
            0.1f,
            150.0f
        )
    }

    fun draw(
        unifProj: Int,
        unifModel: Int,
        unifCamera: Int,
        model: FloatArray
    ) {
        glUniformMatrix4fv(
            unifProj,
            1,
            false,
            mProjection,
            0
        )

        glUniformMatrix4fv(
            unifCamera,
            1,
            false,
            this.model,
            0
        )

        glUniformMatrix4fv(
            unifModel,
            1,
            false,
            model,
            0
        )
    }

}