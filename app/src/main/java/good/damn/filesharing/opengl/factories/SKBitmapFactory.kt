package good.damn.filesharing.opengl.factories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import good.damn.filesharing.Application

class SKBitmapFactory {
    companion object {
        fun createFromAssets(
            assetPath: String
        ): Bitmap {
            val inp = Application.ASSETS
                .open(assetPath)

            val b = BitmapFactory
                .decodeStream(inp)

            inp.close()

            return b
        }
    }
}