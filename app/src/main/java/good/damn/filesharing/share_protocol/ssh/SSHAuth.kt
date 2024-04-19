package good.damn.filesharing.share_protocol.ssh

import good.damn.filesharing.Application
import good.damn.filesharing.utils.ByteUtils
import good.damn.filesharing.utils.CryptoUtils

class SSHAuth(
    val user: String,
    val rsaKey: String? = null,
    val contentOffset: Int
) {
    companion object {
        fun authenticate(
            request: ByteArray
        ): SSHAuth? {

            if (request.isEmpty()) {
                return null
            }

            var credentialLen = request[0]
                .toInt()

            var offset = 1

            var key: String? = null

            if (credentialLen == 1) {
                // Request has RSA-key
                val keyLen = ByteUtils
                    .short(request, 1)

                key = String(
                    request,
                    3,
                    keyLen,
                    Application.CHARSET_ASCII
                )

                offset = 3 + keyLen

                credentialLen = request[offset]
                    .toInt()

                offset++
            }

            val user = CryptoUtils
                .sha256Base64(
                    request,
                    offset,
                    credentialLen
                )

            return SSHAuth(
                user,
                key,
                credentialLen+offset
            )

        }
    }

    override fun toString(): String {
        return "USER: $user\nKEY:$rsaKey\nOFFSET:$contentOffset"
    }
}