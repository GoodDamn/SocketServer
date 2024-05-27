package good.damn.filesharing.share_protocol.interfaces

import java.io.File
import java.io.OutputStream

interface ResponseStreamable {

    fun responseStream(
        out: OutputStream,
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int
    )

}