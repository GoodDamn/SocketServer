package good.damn.filesharing.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.callbacks.ActivityResultCopyToDoc
import good.damn.filesharing.controllers.Messenger
import good.damn.filesharing.controllers.TCPServer
import good.damn.filesharing.controllers.launchers.ContentLauncher
import good.damn.filesharing.listeners.activityResult.ActivityResultCopyListener
import good.damn.filesharing.listeners.network.server.ServerListener
import good.damn.filesharing.listeners.network.service.HotspotServiceListener
import good.damn.filesharing.utils.FileUtils
import good.damn.filesharing.views.ServerView
import java.net.ServerSocket
import java.net.Socket

class TCPServerActivity
    : AppCompatActivity(),
    ServerListener,
    HotspotServiceListener,
    ActivityResultCopyListener {

    private val TAG = "TCPServerActivity"

    private lateinit var mServerView: ServerView<ServerListener>

    @SuppressLint("SetTextI18n")
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        val server = TCPServer(
            8080
        )
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
            server,
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
    override fun onCreateServer(
        server: ServerSocket
    ) {
        Log.d(TAG, "onCreateServer: ")
        mServerView.addMessage(
            "Server started!"
        )
    }

    @WorkerThread
    override fun onStartListen() {
        mServerView.addMessage(
            "Listen clients..."
        )
    }

    @WorkerThread
    override fun onListenChunkData(
        data: ByteArray,
        readBytes: Int,
        last: Int
    ) {
        mServerView.addMessage(
            "$readBytes $last"
        )
    }

    @WorkerThread
    override fun onGetFile(
        data: ByteArray,
        offset: Int,
        fileName: String
    ) {
        val msg = FileUtils.writeToDoc(
            fileName,
            data,
            offset
        )

        if (msg != null) {
            mServerView.addMessage(
                "SAVE PROCESS::EXCEPTION\n$msg"
            )
            return
        }

        mServerView.addMessage(
            "$fileName is saved to Documents"
        )
    }

    @WorkerThread
    override fun onGetText(
        msg: String
    ) {
        mServerView.addMessage(
            msg
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
    override fun onDropClient(
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

    override fun onGetHotspotIP(
        addressList: String
    ) {

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