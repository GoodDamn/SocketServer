package good.damn.filesharing.opengl.maps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import good.damn.filesharing.opengl.factories.SKBitmapFactory
import good.damn.filesharing.utils.ByteUtils

class DisplacementMap(
    assetPath: String,
    context: Context
) {
    companion object {
        private const val TAG = "DisplacementMap"
        private const val MAX_HEIGHT = 3.0f
    }

    private val mBitmap = SKBitmapFactory
        .createFromAssets(assetPath,context)

    private val mColorBytes = ByteArray(4)
    fun getHeight(
        x: Int,
        y: Int
    ): Float {
        val color = mBitmap
            .getPixel(x,y)

        ByteUtils.integer(
            color,
            mColorBytes
        )

        Log.d(TAG, "getHeight: $color ${mColorBytes.contentToString()}")
        return 0.0f
    }
}