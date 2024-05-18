package good.damn.filesharing.share_protocol.method

import good.damn.filesharing.Application
import good.damn.filesharing.services.network.request.HTTPResponseService
import java.io.File
import java.io.OutputStream

class ShareMethodHTTPGet
: ShareMethodStream(
    byteArrayOf(0x47,0x45,0x54) // GET - ASCII Codes
) {
    override fun responseStream(
        out: OutputStream,
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int
    ) {

        val httpMessage = String(
            request,
            Application.CHARSET
        )

        val path = httpMessage
            .substring(
                5,
                httpMessage.indexOf(
                    " ",
                    5
                )
            )

        val response = HTTPResponseService()
        response.execute(
            path
        )
    }

}