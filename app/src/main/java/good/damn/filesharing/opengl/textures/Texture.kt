package good.damn.filesharing.opengl.textures

import android.graphics.BitmapFactory
import android.opengl.GLES30.*
import android.opengl.GLUtils
import good.damn.filesharing.Application
import good.damn.filesharing.utils.FileUtils

class Texture(
    assetPath: String
) {

    private var mId = 0
    private var mUniformTexture = 0

    init {


        val inp = Application.ASSETS.open(
            assetPath
        )

        val bitmap = BitmapFactory.decodeStream(
            inp
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
            mId
        )

        glUniform1i(
            mUniformTexture,
            0
        )
    }
}