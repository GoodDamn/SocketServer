package good.damn.filesharing.shareProtocol.ssh

import good.damn.filesharing.Application
import good.damn.filesharing.utils.CryptoUtils

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

            val user = CryptoUtils
                .sha256Base64(
                    request,
                    1,
                    len
                )

            return SSHAuth(
                user
            )

        }
    }
}