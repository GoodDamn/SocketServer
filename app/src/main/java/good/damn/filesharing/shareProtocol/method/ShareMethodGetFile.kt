package good.damn.filesharing.shareProtocol.method

class ShareMethodGetFile
: ShareMethod(
    byteArrayOf(
        0x67, 0x66, 0 // gf
    )
)