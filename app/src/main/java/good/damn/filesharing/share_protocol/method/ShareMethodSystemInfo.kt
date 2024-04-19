package good.damn.filesharing.share_protocol.method

import android.os.Build
import good.damn.filesharing.Application
import good.damn.filesharing.utils.ResponseUtils
import java.io.File

class ShareMethodSystemInfo
: ShareMethod(
    "info".toByteArray(
        Application.CHARSET_ASCII
    )
){

    companion object {
        private val msg = "ANDROID_VERSION: ${Build.VERSION.CODENAME} ${Build.VERSION.SDK_INT}\n"+
            "DEVICE: ${Build.DEVICE}\n" +
            "HARDWARE ${Build.HARDWARE}" +
            "BRAND: ${Build.BRAND}"
    }

    override fun response(
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int,
        userFolder: File
    ): ByteArray {
        return ResponseUtils.responseMessage16Id(
            msg
        )
    }

}