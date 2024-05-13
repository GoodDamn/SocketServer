package good.damn.filesharing.opengl

import kotlin.math.abs
import kotlin.math.sqrt

class Vector(
    var x: Float,
    var y: Float = 0f,
    var z: Float = 0f
) {

    fun normalize() {
        val len = length()
        x /= len
        y /= len
        z /= len
    }

    fun length(): Float {
        return abs(sqrt(
            x * x + y * y + z * z
        ))
    }

}