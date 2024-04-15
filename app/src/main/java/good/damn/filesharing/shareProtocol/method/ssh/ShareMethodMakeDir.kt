package good.damn.filesharing.shareProtocol.method.ssh

import good.damn.filesharing.shareProtocol.method.ShareMethod

class ShareMethodMakeDir
: ShareMethod(
    byteArrayOf(
        0x6D, 0x6B, 0x64 // mkd
    )
)