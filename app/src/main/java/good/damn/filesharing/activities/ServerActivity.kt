package good.damn.filesharing.activities

import android.annotation.SuppressLint
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.ByteUtils
import good.damn.filesharing.models.Server
import java.io.ByteArrayOutputStream
import java.net.ServerSocket
import java.net.Socket
import java.nio.ByteOrder
import java.nio.charset.Charset

class ServerActivity
    : AppCompatActivity(),
      Server.ServerListener {

    private val TAG = "ServerActivity"

    private var mIndexString = 0

    private lateinit var mTextViewMsg: TextView

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

        mTextViewMsg = TextView(this)
        mTextViewMsg.text = "----"
        mTextViewMsg.textSize = 18f
        mTextViewMsg.movementMethod = ScrollingMovementMethod()
        mTextViewMsg.isVerticalScrollBarEnabled = true
        mTextViewMsg.isHorizontalScrollBarEnabled = false

        val rootLayout = LinearLayout(this)
        rootLayout.gravity = Gravity.CENTER
        rootLayout.orientation = LinearLayout.VERTICAL

        rootLayout.addView(textViewIP,-1,-2)
        rootLayout.addView(btnCreate,-1,-2)
        rootLayout.addView(btnDrop,-1,-2)
        rootLayout.addView(mTextViewMsg, -1,-1)

        btnCreate.setOnClickListener {
            server.create()
        }

        btnDrop.setOnClickListener {
            server.drop()
        }

        setContentView(rootLayout)
    }

    override fun onCreateServer(server: ServerSocket) {
        addMessage("Server started!")
    }

    override fun onListenClient(
        socket: Socket,
        data: ByteArray
    ) {
        val ip = socket.remoteSocketAddress
        addMessage("$ip is connected")

        addMessage(String(data,Charset.
            forName("UTF-8")))

        addMessage("$ip is disconnected")
    }

    override fun onDropServer() {
        addMessage("Server is dropped")
    }

    @SuppressLint("SetTextI18n")
    private fun addMessage(
        text: String
    ) {
        runOnUiThread {
            mTextViewMsg.text = mTextViewMsg.text.toString() + "\n${mIndexString++}) " + text
        }
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