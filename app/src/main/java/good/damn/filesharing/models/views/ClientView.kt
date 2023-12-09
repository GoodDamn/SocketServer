package good.damn.filesharing.models.views

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.widget.*
import good.damn.filesharing.models.Connectable
import good.damn.filesharing.models.Messenger
import java.net.Socket

class ClientView(context: Context) :
    LinearLayout(context),
    Connectable {

    private val msgr = Messenger()
    private var mEditTextHost: EditText
    private var mEditTextPort: EditText
    private var mEditTextMsg: EditText

    init {
        mEditTextHost = EditText(context)
        mEditTextHost.hint = "Enter host ip"

        mEditTextPort = EditText(context)
        mEditTextPort.hint = "Enter host port"

        mEditTextMsg = EditText(context)
        mEditTextMsg.hint = "Message"

        val btnConnect = Button(context)
        btnConnect.text = "Connect to host"

        val textViewMsg = TextView(context)
        textViewMsg.text = "----"
        textViewMsg.textSize = 18f
        textViewMsg.movementMethod = ScrollingMovementMethod()
        textViewMsg.isVerticalScrollBarEnabled = true
        textViewMsg.isHorizontalScrollBarEnabled = false

        msgr.setTextView(textViewMsg)

        btnConnect.setOnClickListener {
            connectToHost(
                mEditTextHost.text.toString(),
                mEditTextPort.text.toString().toInt()
            )
        }

        gravity = Gravity.CENTER
        orientation = VERTICAL

        addView(mEditTextHost, -1, -2)
        addView(mEditTextPort, -1, -2)
        addView(mEditTextMsg, -1, -2)
        addView(btnConnect, -1, -2)
        addView(textViewMsg, -1, -2)
    }

    override fun onConnected(socket: Socket) {
        msgr.addMessage("Connected to ${socket.remoteSocketAddress}")
    }

    override fun onSendBytes(): ByteArray {
        return mEditTextMsg
            .text
            .toString()
            .toByteArray(msgr.getCharset())
    }

    override fun onGetResponse(data: ByteArray) {
        msgr.addMessage(String(data,msgr.getCharset()))
    }

    fun addMessage(
        text: String
    ) {
        msgr.addMessage(text)
    }
}