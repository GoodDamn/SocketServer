package good.damn.filesharing.share_protocol.method

import good.damn.filesharing.Application
import java.io.File

class ShareMethodPowerOff
: ShareMethod(
    "pff".toByteArray(
        Application.CHARSET_ASCII
    )
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