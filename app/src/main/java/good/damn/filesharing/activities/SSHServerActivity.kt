package good.damn.filesharing.activities

import android.os.Bundle
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.Application
import good.damn.filesharing.listeners.network.server.SSHServerListener
import good.damn.filesharing.servers.SSHServer
import good.damn.filesharing.views.ServerView

class SSHServerActivity
: AppCompatActivity(
), SSHServerListener {

    private var mServerView: ServerView<SSHServerListener>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = this

        val server = SSHServer(
            8080,
            ByteArray(300)
        )

        server.delegate = this

        mServerView = ServerView(
            server,
            context
        )

        setContentView(
            mServerView
        )
    }

    @WorkerThread
    override fun onRequestCommand(
        request: ByteArray
    ) {
        val len = request[0]
            .toInt()

        val command = String(
            request,
            1,
            len,
            Application.CHARSET_ASCII
        )

        mServerView?.addMessage(
            "REQUEST_COMMAND:"
        )

        mServerView?.addMessage(
            command
        )
    }

    @WorkerThread
    override fun onResponseBuffer(): ByteArray {
        return ByteArray(0)
    }

}