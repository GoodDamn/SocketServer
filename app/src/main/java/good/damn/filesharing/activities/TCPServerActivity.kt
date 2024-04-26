package good.damn.filesharing.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.Application
import good.damn.filesharing.callbacks.ActivityResultCopyToDoc
import good.damn.filesharing.servers.TCPServer
import good.damn.filesharing.controllers.launchers.ContentLauncher
import good.damn.filesharing.listeners.activityResult.ActivityResultCopyListener
import good.damn.filesharing.listeners.network.server.ServerListener
import good.damn.filesharing.listeners.network.server.TCPServerListener
import good.damn.filesharing.listeners.network.service.HotspotServiceListener
import good.damn.filesharing.servers.BaseServer
import good.damn.filesharing.servers.SSLServer
import good.damn.filesharing.utils.FileUtils
import good.damn.filesharing.views.ServerView
import java.net.ServerSocket
import java.net.Socket

class TCPServerActivity
    : AppCompatActivity(),
    TCPServerListener,
    ActivityResultCopyListener {

    private val TAG = "TCPServerActivity"

    private lateinit var mServerView: ServerView<TCPServerListener>

    @SuppressLint("SetTextI18n")
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        val server = TCPServer(
            8080
        )

        val sslServer = SSLServer(
            4443
        )

        Application.SERVER = server
        Application.SERVER_SSL = sslServer

        sslServer.delegate = this
        server.delegate = this

        val activityResult = ActivityResultCopyToDoc(
            this
        )

        activityResult.delegate = this

        val contentLauncher = ContentLauncher(
            this,
            activityResult
        )

        mServerView = ServerView(
            arrayOf(
                server,
                sslServer
            ),
            this
        )

        val btnPutFile = Button(this)
        btnPutFile.text = "Put file"
        btnPutFile.setOnClickListener {
            contentLauncher.launch("*/*")
        }

        btnPutFile.layoutParams = ViewGroup.LayoutParams(
            -1,
            -2
        )

        mServerView.addView(
            btnPutFile,
            mServerView.childCount - 1
        )

        setContentView(
            mServerView
        )

    }

    @WorkerThread
    override fun onCreateServer() {
        Log.d(TAG, "onCreateServer: ")
        mServerView.addMessage(
            "Server started!"
        )
    }

    @WorkerThread
    override fun onListen() {
        mServerView.addMessage(
            "Listen clients..."
        )
    }

    @WorkerThread
    override fun onHttpGet(
        request: String
    ) {
        mServerView.addMessage(
            "HTTP-GET REQUEST"
        )

        mServerView.addMessage(
            request
        )
    }

    @WorkerThread
    override fun onAcceptClient(
        socket: Socket
    ) {
        mServerView.addMessage(
            "${socket.remoteSocketAddress} accepted"
        )
    }

    @WorkerThread
    override fun onDisconnectClient(
        socket: Socket
    ) {
        mServerView.addMessage(
            "${socket.remoteSocketAddress} is disconnected"
        )
    }

    @WorkerThread
    override fun onDropServer() {
        mServerView.addMessage(
            "Server is dropped"
        )
    }

    override fun onSuccessCopyFile(
        fileName: String
    ) {
        mServerView.addMessage(
            "FILE IS COPIED $fileName"
        )
    }

    override fun onErrorCopyFile(
        errorMsg: String
    ) {
        mServerView.addMessage(
            "ERROR_COPY_FILE:"
        )

        mServerView.addMessage(
            errorMsg
        )
    }

}