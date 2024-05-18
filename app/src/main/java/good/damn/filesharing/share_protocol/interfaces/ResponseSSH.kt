package good.damn.filesharing.share_protocol.interfaces

import java.io.File
import java.io.OutputStream

interface ResponseSSH {
    fun response(
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int,
        userFolder: File
    ): ByteArray
}