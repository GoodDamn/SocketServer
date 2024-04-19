package good.damn.filesharing.share_protocol.ssh

import good.damn.filesharing.Application

class SSHRequest(
    private val mRequest: ByteArray
) {

    var command: String = ""
        private set

    fun decode() {
        val lenAuth = mRequest[0]
            .toInt()
        val off = 1+lenAuth
        val lenCommand = mRequest[off]
            .toInt()

        command = String(
            mRequest,
            off,
            lenCommand,
            Application.CHARSET_ASCII
        )

    }

}