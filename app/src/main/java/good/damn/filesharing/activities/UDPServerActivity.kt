package good.damn.filesharing.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.controllers.Messenger
import good.damn.filesharing.controllers.UDPServer
import good.damn.filesharing.listeners.network.server.UDPServerListener
import java.net.DatagramSocket

class UDPServerActivity
    : AppCompatActivity(),
    UDPServerListener {

    val mUdpServer = UDPServer(
        8080,
        ByteArray(300)
    )

    val msgr = Messenger()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        val context = this

        mUdpServer.delegate = this

        val btnStart = Button(
            context
        )

        val btnStop = Button(
            context
        )

        val textViewMsg = TextView(this)
        textViewMsg.text = "----"
        textViewMsg.textSize = 18f
        textViewMsg.movementMethod = ScrollingMovementMethod()
        textViewMsg.isVerticalScrollBarEnabled = true
        textViewMsg.isHorizontalScrollBarEnabled = false

        msgr.setTextView(
            textViewMsg
        )

        val layout = LinearLayout(
            context
        )

        btnStart.text = "Start Server"
        btnStop.text = "Stop Server"

        btnStart.setOnClickListener(
            this::onClickBtnStart
        )

        btnStop.setOnClickListener(
            this::onClickBtnStop
        )

        layout.orientation = LinearLayout
            .VERTICAL

        layout.addView(
            btnStart,
            -1,
            -2
        )

        layout.addView(
            btnStop,
            -1,
            -2
        )

        layout.addView(
            textViewMsg,
            -1,
            -1
        )

        setContentView(
            layout
        )
    }

    override fun onCreateDatagram(
        socket: DatagramSocket
    ) {
        msgr.addMessage("Listening... on ${socket.port} port")
    }

    override fun onResponse(
        data: ByteArray
    ) {
        msgr.addMessage(
            "RESPONSE:"
        )
        msgr.addMessage(
            data.contentToString()
        )
    }
}

fun UDPServerActivity.onClickBtnStart(
    view: View
) {
    mUdpServer.start()
}

fun UDPServerActivity.onClickBtnStop(
    view: View
) {
    mUdpServer.stop()
}