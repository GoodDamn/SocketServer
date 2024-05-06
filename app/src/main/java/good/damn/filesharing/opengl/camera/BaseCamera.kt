package good.damn.filesharing.opengl.camera

import android.opengl.GLES30.*
import android.opengl.Matrix
import good.damn.filesharing.opengl.entities.DimensionObject

open class BaseCamera(
    width: Int,
    height: Int
): DimensionObject() {

    private val mProjection = FloatArray(
        16
    )

    init {
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