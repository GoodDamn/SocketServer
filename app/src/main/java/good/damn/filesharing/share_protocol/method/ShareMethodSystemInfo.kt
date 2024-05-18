package good.damn.filesharing.share_protocol.method

import android.os.Build
import good.damn.filesharing.Application
import good.damn.filesharing.utils.ResponseUtils
import java.io.File
import java.io.OutputStream

class ShareMethodSystemInfo
: ShareMethodStream(
    "info".toByteArray(
        Application.CHARSET_ASCII
    )
) {

    companion object {
        private val msg = "ANDROID_VERSION: ${Build.VERSION.CODENAME} ${Build.VERSION.SDK_INT}\n"+
            "DEVICE: ${Build.DEVICE}\n" +
            "HARDWARE ${Build.HARDWARE}" +
            "BRAND: ${Build.BRAND}"
    }

    override fun responseStream(
        out: OutputStream,
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int
    ) {
        ResponseUtils.responseMessage16IdStream(
            out,
            msg
        )
    }

}