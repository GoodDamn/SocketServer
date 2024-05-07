package good.damn.filesharing.opengl.camera

import android.opengl.Matrix
import android.util.Log
import kotlin.math.cos
import kotlin.math.log
import kotlin.math.sin

class RotationCamera(
    width: Int,
    height: Int
): BaseCamera(
    width,
    height
) {
    companion object{
        private const val TAG = "RotationCamera"
    }

    var radius: Float = 6f

    private var mHDegrees: Float = 1f
    private var mVDegrees: Float = 1f

    fun rotateBy(
        x: Float,
        y: Float
    ) {
        setRotation(
            mHDegrees + x,
            mVDegrees + y
        )
    }

    fun setRotation(
        x: Float,
        y: Float
    ) {

        mHDegrees = x
        mVDegrees = y

        val ysin = sin(mVDegrees)
        
        setPosition(
            radius * cos(mHDegrees) * ysin,
            radius * cos(mVDegrees),
            radius * ysin * sin(mHDegrees)
        )

        Matrix.setLookAtM(
            model,
            0,
            model[12], model[13], model[14], // Position
            0f, 0f, 0f, // AT
            0f, 1f, 0f
        )
    }

}