package good.damn.filesharing.activities

import android.annotation.SuppressLint
import android.net.LinkAddress
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import good.damn.clientsocket.services.network.HotspotServiceCompat
import good.damn.filesharing.controllers.Messenger
import good.damn.filesharing.controllers.Server
import good.damn.filesharing.controllers.launchers.ContentLauncher
import good.damn.filesharing.listeners.network.server.ServerListener
import good.damn.filesharing.listeners.network.service.HotspotServiceListener
import good.damn.filesharing.utils.FileUtils
import java.net.ServerSocket
import java.net.Socket

class ServerActivity
    : AppCompatActivity(),
    ServerListener,
    HotspotServiceListener,
    ActivityResultCallback<Uri?>{

    private val TAG = "ServerActivity"

    private val msgr = Messenger()
    private val mPort = 8080

    private lateinit var mTextViewIP: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val server = Server(mPort)
        server.setOnServerListener(this)

        val contentLauncher = ContentLauncher(
            this,
            this
        )

        mTextViewIP = TextView(this)
        mTextViewIP.textSize = 18.0f

        val btnCreate = Button(this)
        btnCreate.text = "Create server"

        val btnDrop = Button(this)
        btnDrop.text = "Drop server"

        val btnResponseFile = Button(this)
        btnResponseFile.text = "Select response file"

        val editTextMsg = EditText(this)
        editTextMsg.hint = "Message"

        val textViewMsg = TextView(this)
        textViewMsg.text = "----"
        textViewMsg.textSize = 18f
        textViewMsg.movementMethod = ScrollingMovementMethod()
        textViewMsg.isVerticalScrollBarEnabled = true
        textViewMsg.isHorizontalScrollBarEnabled = false

        msgr.setTextView(textViewMsg)

        val btnPutFile = Button(this)
        btnPutFile.text = "Put file"
        btnPutFile.setOnClickListener {
            contentLauncher.launch("*/*")
        }


        val rootLayout = LinearLayout(this)
        rootLayout.gravity = Gravity.CENTER
        rootLayout.orientation = LinearLayout.VERTICAL

        rootLayout.addView(
            mTextViewIP,
            -1,
            -2
        )

        rootLayout.addView(
            btnCreate,
            -1,
            -2
        )

        rootLayout.addView(
            btnDrop,
            -1,
            -2
        )

        rootLayout.addView(
            editTextMsg,
            -1,
            -2
        )

        rootLayout.addView(
            btnPutFile,
            -1,
            -2
        )

        rootLayout.addView(
            textViewMsg,
            -1,
            -1
        )

        btnResponseFile.setOnClickListener {
            contentLauncher.launch("*/*")
        }

        btnCreate.setOnClickListener {
            server.create()
        }

        btnDrop.setOnClickListener {
            server.drop()
        }

        val hotspotService = HotspotServiceCompat(
            this
        )

        hotspotService.delegate = this
        setContentView(rootLayout)

        hotspotService.start()
    }

    @WorkerThread
    override fun onCreateServer(server: ServerSocket) {
        msgr.addMessage("Server started!")
    }

    @WorkerThread
    override fun onStartListen() {
        msgr.addMessage("Listen clients...")
    }

    @WorkerThread
    override fun onListenChunkData(
        data: ByteArray,
        readBytes: Int,
        last: Int
    ) {
        msgr.addMessage("$readBytes $last")
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
            msgr.addMessage("SAVE PROCESS::EXCEPTION\n$msg")
            return
        }

        msgr.addMessage("$fileName is saved to Documents")
    }

    @WorkerThread
    override fun onGetText(msg: String) {
        msgr.addMessage(msg)
    }

    @WorkerThread
    override fun onHttpGet(
        request: String
    ) {
        msgr.addMessage("HTTP-GET REQUEST")
        msgr.addMessage(request)
    }

    @WorkerThread
    override fun onDropClient(socket: Socket) {
        msgr.addMessage("${socket.remoteSocketAddress} is disconnected")
    }

    @WorkerThread
    override fun onDropServer() {
        msgr.addMessage("Server is dropped")
    }

    override fun onGetHotspotIP(
        addressList: String
    ) {
        mTextViewIP.text = "Host: $addressList\nPort: $mPort"
    }

    override fun onActivityResult(
        uri: Uri?
    ) {
        val data = FileUtils
            .read(uri, this)

        if (data == null) {
            Toast.makeText(
                this,
                "Something went wrong",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val p = uri!!.path!!
        val t = "primary:"
        val filePath = p.substring(p.indexOf(t) + t.length)

        val nameIndex = filePath.lastIndexOf("/")

        val fileName = if (nameIndex == -1)
            filePath
        else filePath.substring(nameIndex + 1)

        FileUtils.writeToDoc(
            fileName,
            data,
            0
        )

        Toast.makeText(
            this,
            "FILE IS COPIED $fileName",
            Toast.LENGTH_SHORT
        ).show()
    }

}