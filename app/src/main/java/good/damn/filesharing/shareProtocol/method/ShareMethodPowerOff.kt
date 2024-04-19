package good.damn.filesharing.shareProtocol.method

import android.util.Log
import good.damn.filesharing.Application
import java.io.File
import java.net.ServerSocket

class ShareMethodPowerOff
: ShareMethod(
    "pff".toByteArray(
        Application.CHARSET_ASCII
    ),
    length = 3
){

    companion object {
        private const val TAG = "ShareMethodPowerOff"
    }

    override fun response(
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int,
        userFolder: File
    ): ByteArray {
        Application.SERVER?.stop()
        Application.SERVER_SSL?.stop()
        return ByteArray(0)
    }

}