package good.damn.filesharing.shareProtocol.ssh

import good.damn.filesharing.Application

class SSHAuth(
    val user: String
) {
    companion object {
        fun authenticate(
            request: ByteArray
        ): SSHAuth? {

            if (request.isEmpty()) {
                return null
            }

            val len = request[0]
                .toInt()

            val user = String(
                request,
                1,
                len,
                Application.CHARSET_ASCII
            )

            return SSHAuth(
                user
            )

        }
    }
}