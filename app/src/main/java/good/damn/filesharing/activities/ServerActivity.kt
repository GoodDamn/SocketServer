package good.damn.filesharing.activities

import android.annotation.SuppressLint
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.ByteUtils
import good.damn.filesharing.models.Messenger
import good.damn.filesharing.models.Server
import java.net.ServerSocket
import java.net.Socket
import java.nio.ByteOrder
import java.nio.charset.Charset

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

        val textViewIP = TextView(this)
        textViewIP.text = "Host: $ip\nPort: $port"
        textViewIP.textSize = 18.0f

        val btnCreate = Button(this)
        btnCreate.text = "Create server"

        val btnDrop = Button(this)
        btnDrop.text = "Drop server"

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
        rootLayout.addView(textViewMsg, -1,-1)

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
                String(data,Charset.forName("UTF-8"))
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