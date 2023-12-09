package good.damn.filesharing.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.models.Messenger
import java.net.Socket
import java.nio.charset.Charset

class ClientActivity: AppCompatActivity() {

    private val msgr = Messenger()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val editTextHost = EditText(this)
        editTextHost.hint = "Enter host ip"

        val editTextPort = EditText(this)
        editTextPort.hint = "Enter host port"

        val btnConnect = Button(this)
        btnConnect.text = "Connect to host"

        val textViewMsg = TextView(this)
        textViewMsg.text = "----"
        textViewMsg.textSize = 18f
        textViewMsg.movementMethod = ScrollingMovementMethod()
        textViewMsg.isVerticalScrollBarEnabled = true
        textViewMsg.isHorizontalScrollBarEnabled = false

        msgr.setTextView(textViewMsg)

        btnConnect.setOnClickListener {
            connectToHost(
                editTextHost.text.toString(),
                editTextPort.text.toString().toInt()
            )
        }

        val rootLayout = LinearLayout(this)
        rootLayout.gravity = Gravity.CENTER
        rootLayout.orientation = LinearLayout.VERTICAL

        rootLayout.addView(editTextHost,-1,-2)
        rootLayout.addView(editTextPort,-1,-2)
        rootLayout.addView(btnConnect, -1,-2)
        rootLayout.addView(textViewMsg,-1,-2)

        setContentView(rootLayout)
    }


    private fun connectToHost(
        host: String,
        port: Int
    ) {
        Thread {
            val socket = Socket(host,port)

            msgr.addMessage("Connected to $host")

            val out = socket.getOutputStream()

            out.write("Hello! I'm your client!".toByteArray(
                Charset.forName("UTF-8")
            ))

            out.close()
            socket.close()
        }.start()
    }

}