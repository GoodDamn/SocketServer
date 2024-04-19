package good.damn.filesharing.share_protocol.method

import good.damn.filesharing.Application
import good.damn.filesharing.services.network.request.HTTPResponseService
import java.io.File

class ShareMethodHTTPGet
: ShareMethod(
    byteArrayOf(0x47,0x45,0x54) // GET - ASCII Codes
) {
    override fun response(
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int,
        userFolder: File
    ): ByteArray {

        val httpMessage = String(
            request,
            Application.CHARSET
        )

        //delegate?.onHttpGet(httpMessage)

        val path = httpMessage
            .substring(
                5,
                httpMessage.indexOf(
                    " ",
                    5
                )
            )

        val response = HTTPResponseService()
        return response.execute(
            path
        )
    }

}