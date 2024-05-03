package good.damn.filesharing.opengl.entities.primitives

import good.damn.filesharing.opengl.entities.Entity
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Plane(
    mProgram: Int
): Entity(
    floatArrayOf(
        -0.5f, -0.5f, 0.0f,
        -0.5f,  0.5f, 0.0f,
         0.5f,  0.5f, 0.0f,
         0.5f, -0.5f, 0.0f
    ),
    shortArrayOf(
        0, 1, 2,
        0, 2, 3
    ),
    mProgram
)