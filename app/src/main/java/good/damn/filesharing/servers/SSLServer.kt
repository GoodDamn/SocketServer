package good.damn.filesharing.servers

import android.content.res.Resources
import androidx.annotation.WorkerThread
import good.damn.filesharing.Application
import java.net.ServerSocket

class SSLServer(
    private val resources: Resources,
    port: Int = 4443
) : TCPServer(
   port
) {

    override fun serverType(): String {
        return "SSL"
    }

    @WorkerThread
    override fun onCreateSocket(): ServerSocket {
        return Application
            .createSSLContext(
                resources
            )
            .serverSocketFactory
            .createServerSocket(
                port
            )
    }
}