package good.damn.filesharing.activities

import android.os.Bundle
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.Application
import good.damn.filesharing.listeners.network.server.SSHServerListener
import good.damn.filesharing.servers.SSHServer
import good.damn.filesharing.views.ServerView
import java.io.BufferedReader
import java.io.InputStreamReader

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
    override fun onCredentials(
        request: ByteArray
    ) {

    }

    @WorkerThread
    override fun onResponseBuffer(): ByteArray {
        return ByteArray(0)
    }

}