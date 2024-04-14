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

        val process = Runtime.getRuntime()
            .exec(command)

        val buf = BufferedReader(
            InputStreamReader(
                process.inputStream
            )
        )

        val bufError = BufferedReader(
            InputStreamReader(
                process.errorStream
            )
        )


        mServerView?.addMessage(
            "RESULT:"
        )
        while(true) {
            val line = buf.readLine() ?: break
            mServerView?.addMessage(
                line
            )
        }

        mServerView?.addMessage(
            "ERROR:"
        )
        while(true) {
            val line = bufError.readLine() ?: break
            mServerView?.addMessage(
                line
            )
        }

        buf.close()
        bufError.close()
        process.waitFor()
    }

    @WorkerThread
    override fun onResponseBuffer(): ByteArray {
        return ByteArray(0)
    }

}