package good.damn.filesharing.shareProtocol.method

class ShareMethodHTTPGet
: ShareMethod(
    byteArrayOf(0x47,0x45,0x54) // GET - ASCII Codes
)