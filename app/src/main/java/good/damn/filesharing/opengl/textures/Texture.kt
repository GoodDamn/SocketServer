package good.damn.filesharing.opengl.textures

import android.graphics.BitmapFactory
import android.opengl.GLES30.*
import android.opengl.GLUtils
import good.damn.filesharing.Application
import good.damn.filesharing.utils.FileUtils

class Texture(
    assetPath: String,
    program: Int
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
            bitmap,
            0
        )

        mUniformTexture = glGetUniformLocation(
            program,
            "texture"
        )

    }

    fun draw() {
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