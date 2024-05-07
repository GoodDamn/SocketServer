package good.damn.filesharing.opengl.light

import good.damn.filesharing.opengl.entities.DimensionObject
import java.nio.IntBuffer

import android.opengl.GLES30.*
import android.opengl.Matrix
import good.damn.filesharing.opengl.Vector

class DirectionalLight(
    program: Int
): DimensionObject() {

    private val mUniformColor: Int
    //private val mUniformAmbient: Int
    //private val mUniformDirection: Int
    private val mUniformIntensity: Int
    /*private val mUniformSpecular: Int
    private val mShininess: Int*/

    private val mDirectionVector = Vector(
        1f,
        1f,
        1f
    )

    init {
        mDirectionVector.normalize()

        mUniformColor = glGetUniformLocation(
            program,
            "light.color"
        )

        /*mUniformAmbient = glGetUniformLocation(
            program,
            "light.ambient"
        )

        mUniformDirection = glGetUniformLocation(
            program,
            "light.direction"
        )*/

        mUniformIntensity = glGetUniformLocation(
            program,
            "light.intensity"
        )

    }

    override fun setPosition(
        x: Float,
        y: Float,
        z: Float
    ) {
        mDirectionVector.x = x
        mDirectionVector.y = y
        mDirectionVector.z = z
        mDirectionVector.normalize()
    }

    fun draw() {

        glUniform3f(
            mUniformColor,
            1f,
            1f,
            1f
        )

        /*glUniform1f(
            mUniformAmbient,
            0.1f
        )

        glUniform3f(
            mUniformDirection,
            mDirectionVector.x,
            mDirectionVector.y,
            mDirectionVector.z,
        )*/

        glUniform1f(
            mUniformIntensity,
            0.7f
        )
    }

}