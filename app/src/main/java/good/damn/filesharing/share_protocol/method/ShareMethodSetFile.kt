package good.damn.filesharing.share_protocol.method

import android.util.Log
import good.damn.filesharing.Application
import good.damn.filesharing.utils.ByteUtils
import good.damn.filesharing.utils.FileUtils
import good.damn.filesharing.utils.ResponseUtils
import java.io.File
import java.io.OutputStream

class ShareMethodSetFile
: ShareMethodStream(
    "sf".toByteArray(
        Application.CHARSET_ASCII
    )
) {

    override fun responseStream(
        out: OutputStream,
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int
    ) {

        // TO DO: Capture a input stream to safe RAM
        // If it has a large file

        var position = argsPosition

        val fileLen = request[
            position
        ].toInt()

        position++
        val fileName = String(
            request,
            position,
            fileLen,
            Application.CHARSET_ASCII
        )

        Log.d("ShareMethodSetFile:", "response: $fileName $argsCount")


        position += fileLen
        val dataLen = ByteUtils
            .integer(
                request,
                position
            )
        position += 4
        FileUtils.writeToDoc(
            fileName,
            request,
            position,
            dataLen
        )

        ResponseUtils.responseMessageIdStream(
            out,
            "File $fileName has been saved"
        )
    }

}