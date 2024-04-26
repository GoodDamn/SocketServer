package good.damn.filesharing.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.servers.UDPServer
import good.damn.filesharing.listeners.network.server.UDPServerListener
import good.damn.filesharing.views.ServerView
import java.net.DatagramSocket

class UDPServerActivity
    : AppCompatActivity(),
    UDPServerListener {

    private lateinit var mServerView: ServerView<UDPServerListener>

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        val context = this

        val server = UDPServer(
            8080,
            ByteArray(300)
        )

        mServerView = ServerView(
            server,
            context
        )

        setContentView(
            mServerView
        )
    }

    override fun onCreateDatagram(
        socket: DatagramSocket
    ) {
        mServerView.addMessage("Listening... on ${socket.port} port")
    }

    override fun onResponse(
        data: ByteArray
    ) {
        mServerView.addMessage(
            "RESPONSE:"
        )
        mServerView.addMessage(
            data.contentToString()
        )
    }

    override fun onCreateServer() {
        mServerView.addMessage(
            "Server started!"
        )
    }

    override fun onDropServer() {
        mServerView.addMessage(
            "Server stopped"
        )
    }
}