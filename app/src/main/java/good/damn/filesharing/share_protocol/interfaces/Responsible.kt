package good.damn.filesharing.share_protocol.interfaces

import java.io.File

interface Responsible {

    fun response(
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int,
        userFolder: File
    ): ByteArray

}