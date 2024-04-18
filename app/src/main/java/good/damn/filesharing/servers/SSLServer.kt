package good.damn.filesharing.servers

import androidx.annotation.WorkerThread
import good.damn.filesharing.Application
import java.net.ServerSocket

class SSLServer(
    port: Int = 4443
) : TCPServer(
   port
) {

    @WorkerThread
    override fun onCreateSocket(): ServerSocket {
        return Application
            .SSL
            .serverSocketFactory
            .createServerSocket(
                port
            )
    }
}