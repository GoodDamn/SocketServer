package good.damn.filesharing.activities

import android.annotation.SuppressLint
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.utils.ByteUtils
import good.damn.filesharing.models.Messenger
import good.damn.filesharing.models.Server
import good.damn.filesharing.models.launchers.ContentLauncher
import good.damn.filesharing.utils.NetworkUtils
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
            if (uri == null) {
                return@ContentLauncher
            }
            val inp = contentResolver.openInputStream(uri) ?: return@ContentLauncher

            val data = NetworkUtils
                .readBytes(inp)

            inp.close()
            server.setResponse(data)
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
        rootLayout.addView(btnResponseFile, -1,-2)
        rootLayout.addView(editTextMsg, -1, -2)
        rootLayout.addView(textViewMsg, -1,-1)

        editTextMsg.addTextChangedListener(object:TextWatcher {
            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (s == null) {
                    return
                }
                server.setResponseText(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
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

    override fun onCreateServer(server: ServerSocket) {
        msgr.addMessage("Server started!")
    }

    override fun onStartListen() {
        msgr.addMessage("Listen clients...")
    }

    override fun onListenData(
        socket: Socket,
        data: ByteArray
    ) {
        msgr.addMessage("${socket.remoteSocketAddress}\n"+
                String(data,msgr.getCharset())
        )
    }

    override fun onListenClient(
        socket: Socket,
        data: ByteArray
    ) {
        msgr.addMessage("${socket.remoteSocketAddress}\n READY TO RESPONSE")
    }

    override fun onDropClient(socket: Socket) {
        msgr.addMessage("${socket.remoteSocketAddress} is disconnected")
    }

    override fun onDropServer() {
        msgr.addMessage("Server is dropped")
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun getLanIP(): String {
        val wifi = applicationContext.getSystemService(WIFI_SERVICE)
                as WifiManager

        var ip = wifi.connectionInfo.ipAddress

        if (ByteOrder.nativeOrder()
                .equals(ByteOrder.LITTLE_ENDIAN)
        ) {
            ip = Integer.reverseBytes(ip)
        }

        val arr = ByteUtils.integer(ip)
        return "${arr[0]}.${arr[1]}.${arr[2]}.${arr[3]}"
    }
}