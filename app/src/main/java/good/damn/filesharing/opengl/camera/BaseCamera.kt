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

    private var mResult = FloatArray(
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
            0f
        )

        Matrix.perspectiveM(
            mProjection,
            0,
            85.0f / 180.0f * Math.PI.toFloat(),
            width.toFloat() / height.toFloat(),
            1.0f,
            150.0f
        )
    }

    fun draw(
        unifProj: Int,
        unifModel: Int,
        model: FloatArray
    ) {
        glUniformMatrix4fv(
            unifProj,
            1,
            false,
            mProjection,
            0
        )

        Matrix.multiplyMM(
            mResult,
            0,
            this.model,
            0,
            model,
            0
        )

        glUniformMatrix4fv(
            unifModel,
            1,
            false,
            mResult,
            0
        )
    }

}