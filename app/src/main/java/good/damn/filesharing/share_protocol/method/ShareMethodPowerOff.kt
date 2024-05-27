package good.damn.filesharing.share_protocol.method

import good.damn.filesharing.Application
import java.io.File
import java.io.OutputStream

class ShareMethodPowerOff
: ShareMethodStream(
    "pff".toByteArray(
        Application.CHARSET_ASCII
    )
) {

    companion object {
        private const val TAG = "ShareMethodPowerOff"
    }

    override fun responseStream(
        out: OutputStream,
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int
    ) {
        Application.SERVER?.stop()
        Application.SERVER_SSL?.stop()
    }

}