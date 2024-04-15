package good.damn.filesharing.listeners.network.server

import good.damn.filesharing.shareProtocol.ssh.SSHAuth
import good.damn.filesharing.shareProtocol.ssh.SSHRequest

interface SSHServerListener {
    fun onCredentials(
        auth: SSHAuth,
        request: SSHRequest
    )

    fun onResponseBuffer(): ByteArray
}