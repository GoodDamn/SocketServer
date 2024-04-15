package good.damn.filesharing.listeners.network.server

import good.damn.filesharing.shareProtocol.ssh.SSHAuth
import good.damn.filesharing.shareProtocol.ssh.SSHRequest

interface SSHServerListener {
    fun onAuth(
        user: String
    )

    fun onErrorAuth(
        error: String
    )

    fun onResponseBuffer(): ByteArray
}