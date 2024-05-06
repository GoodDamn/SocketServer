package good.damn.filesharing.opengl.entities

import android.opengl.Matrix
import android.opengl.Matrix.*
import android.util.Log

open class DimensionObject {

    companion object {
        private const val TAG = "DimensionObject"
    }

    internal var model = FloatArray(16)

    init {
        setIdentityM(
            model,
            0
        )
    }

    fun setPosition(
        x: Float,
        y: Float,
        z: Float
    ) {
        Log.d(TAG, "setPosition: ${model[12]} ${model[13]} ${model[14]}")
        model[12] = x
        model[13] = y
        model[14] = z
    }
}
