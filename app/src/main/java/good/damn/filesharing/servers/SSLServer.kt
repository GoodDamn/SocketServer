package good.damn.filesharing.servers

import android.content.res.Resources
import androidx.annotation.WorkerThread
import good.damn.filesharing.Application
import java.net.ServerSocket
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLServerSocket

class SSLServer(
    port: Int = 4443
) : TCPServer(
   port
) {

    override fun serverType(): String {
        return "SSL"
    }

    @WorkerThread
    override fun onCreateSocket(): ServerSocket {
        val socket = SSLContext
            .getInstance("TLS")
            .serverSocketFactory
            .createServerSocket(
                port
            )

        (socket as SSLServerSocket)
            .enabledProtocols = arrayOf(
                "TLSv1.1",
                "TLSv1.2"
            )

        return socket
    }
}