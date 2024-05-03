package good.damn.filesharing.opengl.entities.primitives

import good.damn.filesharing.opengl.entities.Entity
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Plane(
    mProgram: Int
): Entity(
    floatArrayOf(
        -1.0f, -1.0f,
        -1.0f,  1.0f,
         1.0f,  1.0f,
         1.0f, -1.0f
    ),
    shortArrayOf(
        0, 1, 2,
        0, 2, 3
    ),
    mProgram
)