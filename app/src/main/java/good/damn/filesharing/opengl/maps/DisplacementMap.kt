package good.damn.filesharing.opengl.maps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import good.damn.filesharing.opengl.factories.SKBitmapFactory
import good.damn.filesharing.utils.ByteUtils
import kotlin.math.roundToInt

class DisplacementMap(
    assetPath: String
) {
    companion object {
        private const val TAG = "DisplacementMap"
        private const val MAX_HEIGHT = 10.0f
    }

    private val mBitmap = SKBitmapFactory
        .createFromAssets(assetPath)

    private val mBitmapWidth = mBitmap.width
    private val mBitmapHeight = mBitmap.height

    private val mColorBytes = ByteArray(4)

    fun getHeightRatio(
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ): Float {
        return getHeight(
            (x / width.toFloat() * mBitmapWidth).toInt(),
            (y / height.toFloat() * mBitmapHeight).toInt()
        )
    }

    fun getHeight(
        x: Int,
        y: Int
    ): Float {
        val xx = if (x == mBitmapWidth) x-1
            else x
        val yy = if (y == mBitmapHeight) y-1
            else y

        Log.d(TAG, "getHeight: COORDS: $xx;$mBitmapWidth $yy $mBitmapHeight")
        val color = mBitmap
            .getPixel(xx,yy)

        ByteUtils.integer(
            color,
            mColorBytes
        )
        val digitalHeight = mColorBytes[1]
            .toInt() and 0xff

        val height = (digitalHeight / 255f) * MAX_HEIGHT
        Log.d(TAG, "getHeight: $color $digitalHeight $height")
        return height
    }
}