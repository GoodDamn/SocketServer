package good.damn.filesharing.opengl.textures

import android.graphics.BitmapFactory
import android.opengl.GLES30.*
import android.opengl.GLUtils
import good.damn.filesharing.Application
import good.damn.filesharing.utils.FileUtils

class Texture(
    assetPath: String
) {

    private var mId = intArrayOf(
        1
    )
    private var mUniformTexture = 0

    init {


        val inp = Application.ASSETS.open(
            assetPath
        )

        val bitmap = BitmapFactory.decodeStream(
            inp
        )


        glGenTextures(
            1,
            mId,
            0
        )

        glBindTexture(
            GL_TEXTURE_2D,
            mId[0]
        )

        glTexParameteri(
            GL_TEXTURE_2D,
            GL_TEXTURE_MIN_FILTER,
            GL_NEAREST
        )

        glTexParameteri(
            GL_TEXTURE_2D,
            GL_TEXTURE_MAG_FILTER,
            GL_LINEAR
        )

        glTexParameteri(
            GL_TEXTURE_2D,
            GL_TEXTURE_WRAP_S,
            GL_CLAMP_TO_EDGE
        )

        glTexParameteri(
            GL_TEXTURE_2D,
            GL_TEXTURE_WRAP_T,
            GL_CLAMP_TO_EDGE
        )

        GLUtils.texImage2D(
            GL_TEXTURE_2D,
            0,
            GL_RGBA,
            bitmap,
            GL_UNSIGNED_BYTE,
            0
        )

    }

    fun setUniformTo(
        program: Int
    ) {
        mUniformTexture = glGetUniformLocation(
            program,
            "texture"
        )
    }

    fun draw() {
        glActiveTexture(
            GL_TEXTURE_2D
        )

        glBindTexture(
            GL_TEXTURE_2D,
            mId[0]
        )

        glUniform1i(
            mUniformTexture,
            0
        )
    }
}