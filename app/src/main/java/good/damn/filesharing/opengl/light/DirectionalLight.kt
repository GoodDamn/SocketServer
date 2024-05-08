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
    private val mUniformAmbient: Int
    private val mUniformDirection: Int
    private val mUniformIntensity: Int
    private val mUniformSpecular: Int
    private val mUniformShininess: Int

    private val mDirectionVector = Vector(
        1f,
        1f,
        -100f
    )

    init {
        mDirectionVector.normalize()

        mUniformColor = glGetUniformLocation(
            program,
            "light.color"
        )

        mUniformAmbient = glGetUniformLocation(
            program,
            "light.ambient"
        )

        mUniformDirection = glGetUniformLocation(
            program,
            "light.direction"
        )

        mUniformIntensity = glGetUniformLocation(
            program,
            "light.intensity"
        )

        mUniformSpecular = glGetUniformLocation(
            program,
            "specularIntensity"
        )

        mUniformShininess = glGetUniformLocation(
            program,
            "shine"
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

        glUniform1f(
            mUniformAmbient,
            0.3f
        )

        glUniform1f(
            mUniformIntensity,
            0.8f
        )

        glUniform3f(
            mUniformDirection,
            mDirectionVector.x,
            mDirectionVector.y,
            mDirectionVector.z,
        )

        glUniform1f(
            mUniformShininess,
            15.0f
        )

        glUniform1f(
            mUniformSpecular,
            2.0f
        )
    }

}