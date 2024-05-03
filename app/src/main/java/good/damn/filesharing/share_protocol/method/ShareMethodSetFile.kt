package good.damn.filesharing.share_protocol.method

import android.util.Log
import good.damn.filesharing.Application
import good.damn.filesharing.utils.ByteUtils
import good.damn.filesharing.utils.FileUtils
import good.damn.filesharing.utils.ResponseUtils
import java.io.File

class ShareMethodSetFile
: ShareMethod(
    "sf".toByteArray(
        Application.CHARSET_ASCII
    )
) {

    override fun response(
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int,
        userFolder: File
    ): ByteArray {

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
        return ResponseUtils.responseMessageId(
            "File $fileName has been saved"
        )
    }

}