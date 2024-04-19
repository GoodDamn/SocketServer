package good.damn.filesharing.listeners.network.server

interface SSHServerListener {
    fun onAuth(
        user: String
    )

    fun onErrorAuth(
        error: String
    )
}