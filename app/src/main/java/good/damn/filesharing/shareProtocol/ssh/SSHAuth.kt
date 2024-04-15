package good.damn.filesharing.shareProtocol.ssh

import good.damn.filesharing.Application

class SSHAuth(
    request: ByteArray
) {

    val user: String

    init {
        val len = request[0]
            .toInt()

        user = String(
            request,
            1,
            len,
            Application.CHARSET_ASCII
        )

    }

}