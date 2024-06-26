package good.damn.filesharing.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.activities.other.ssh.SSHSettingsActivity
import good.damn.filesharing.listeners.network.server.SSHServerListener
import good.damn.filesharing.servers.SSHServer
import good.damn.filesharing.utils.FileUtils
import good.damn.filesharing.views.ServerView
import java.io.File

class SSHServerActivity
: AppCompatActivity(),
SSHServerListener {

    private var mServerView: ServerView<SSHServerListener>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = this

        val buffer = ByteArray(
            4096
        )

        val rsaKeys = FileUtils
            .getUsersRsa(
                buffer
            )

        val server = SSHServer(
            8080,
            rsaKeys,
            buffer
        )

        server.delegate = this

        val btnSettings = Button(
            context
        )

        mServerView = ServerView(
            server,
            context
        )

        btnSettings.text = "Settings"
        btnSettings.layoutParams = ViewGroup
            .LayoutParams(-1,-2)

        btnSettings.setOnClickListener(
            this::onClickBtnSettings
        )

        mServerView!!.addView(
            btnSettings,
            mServerView!!.childCount - 1
        )

        setContentView(
            mServerView
        )
    }

    override fun onAuth(
        user: String
    ) {
        mServerView?.addMessage(
            "USER:"
        )
        mServerView?.addMessage(
            user
        )
    }

    override fun onErrorAuth(
        error: String
    ) {
        mServerView?.addMessage(
            "ERROR:"
        )

        mServerView?.addMessage(
            error
        )
    }

    override fun onCreateServer() {
        mServerView?.addMessage(
            "Server started!"
        )
    }

    override fun onDropServer() {
        mServerView?.addMessage(
            "Server stopped"
        )
    }

}


private fun SSHServerActivity.onClickBtnSettings(
    view: View
) {
    startActivity(
        Intent(
            view.context,
            SSHSettingsActivity::class.java
        )
    )
}