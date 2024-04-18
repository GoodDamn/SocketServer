package good.damn.filesharing.shareProtocol.interfaces

import java.io.File

interface Responsible {

    fun response(
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int,
        userFolder: File
    ): ByteArray

}