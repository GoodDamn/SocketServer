package good.damn.filesharing.utils

import android.opengl.GLES20

class ShaderUtils {
    companion object {
        fun createShader(
            type: Int,
            source: String
        ): Int {
            val shader = GLES20.glCreateShader(
                type
            )

            GLES20.glShaderSource(
                shader,
                source
            )

            GLES20.glCompileShader(
                shader
            )

            return shader
        }
    }
}