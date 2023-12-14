package good.damn.filesharing.activities

import android.annotation.SuppressLint
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.utils.ByteUtils
import good.damn.filesharing.models.Messenger
import good.damn.filesharing.models.Server
import good.damn.filesharing.models.launchers.ContentLauncher
import good.damn.filesharing.utils.FileUtils
import good.damn.filesharing.utils.NetworkUtils
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.nio.ByteOrder

class ServerActivity
    : AppCompatActivity(),
      Server.ServerListener {

    private val TAG = "ServerActivity"

    private val msgr = Messenger()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val port = 1250
        val ip = getLanIP()

        val server = Server(port)
        server.setOnServerListener(this)

        val contentLauncher = ContentLauncher(this) { uri ->
            val data = FileUtils
                .read(uri,this)

            if (data == null) {
                Toast.makeText(
                    this,
                    "Something went wrong",
                    Toast.LENGTH_SHORT)
                    .show()
                return@ContentLauncher
            }

            val p = uri!!.path!!
            val t = "primary:"
            val fileName = p.substring(p.indexOf(t)+t.length)

            server.setResponse(data,fileName)
            Toast.makeText(
                this,
                "FILE IS PREPARED",
                Toast.LENGTH_SHORT)
                .show()
        }

        val textViewIP = TextView(this)
        textViewIP.text = "Host: $ip\nPort: $port"
        textViewIP.textSize = 18.0f

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

        val rootLayout = LinearLayout(this)
        rootLayout.gravity = Gravity.CENTER
        rootLayout.orientation = LinearLayout.VERTICAL

        rootLayout.addView(textViewIP,-1,-2)
        rootLayout.addView(btnCreate,-1,-2)
        rootLayout.addView(btnDrop,-1,-2)
        rootLayout.addView(editTextMsg, -1,-2)
        rootLayout.addView(btnResponseFile, -1,-2)
        rootLayout.addView(textViewMsg, -1,-1)

        editTextMsg.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                server.setResponseText(s.toString())
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        btnResponseFile.setOnClickListener {
            contentLauncher.launch("*/*")
        }

        btnCreate.setOnClickListener {
            server.create()
        }

        btnDrop.setOnClickListener {
            server.drop()
        }

        setContentView(rootLayout)
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
            offset)

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
    override fun onHttpGet(out: OutputStream) {
        msgr.addMessage("HTTP-GET REQUEST")
    }

    @WorkerThread
    override fun onDropClient(socket: Socket) {
        msgr.addMessage("${socket.remoteSocketAddress} is disconnected")
    }

    @WorkerThread
    override fun onDropServer() {
        msgr.addMessage("Server is dropped")
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun getLanIP(): String {
        val wifi = applicationContext.getSystemService(WIFI_SERVICE)
                as WifiManager

        var ip = wifi.connectionInfo.ipAddress

        Log.d(TAG, "getLanIP: WIFI_IP: $ip")

        if (ByteOrder.nativeOrder()
                .equals(ByteOrder.LITTLE_ENDIAN)
        ) {
            ip = Integer.reverseBytes(ip)
        }

        val arr = ByteUtils.integer(ip)
        return "${arr[0]}.${arr[1]}.${arr[2]}.${arr[3]}"
    }
}