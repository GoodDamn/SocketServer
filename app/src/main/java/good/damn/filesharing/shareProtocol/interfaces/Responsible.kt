package good.damn.filesharing.shareProtocol.interfaces

interface Responsible {

    fun response(
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int
    ): ByteArray

}