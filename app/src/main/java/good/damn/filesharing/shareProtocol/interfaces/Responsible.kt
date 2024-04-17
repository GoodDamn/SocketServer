package good.damn.filesharing.shareProtocol.interfaces

interface Responsible {

    fun response(
        request: ByteArray,
        offset: Int
    ): ByteArray

}