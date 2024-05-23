package good.damn.filesharing.opengl.factories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class SKBitmapFactory {
    companion object {
        fun createFromAssets(
            assetPath: String,
            context: Context
        ): Bitmap {
            val inp = context.assets
                .open(assetPath)

            val b = BitmapFactory
                .decodeStream(inp)

            inp.close()

            return b
        }
    }
}