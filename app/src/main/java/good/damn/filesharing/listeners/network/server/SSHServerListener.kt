package good.damn.filesharing.listeners.network.server

interface SSHServerListener
    : ServerListener {
    fun onAuth(
        user: String
    )

    fun onErrorAuth(
        error: String
    )
}