package good.damn.filesharing.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.net.Socket

class ClientActivity: AppCompatActivity() {

    private lateinit var mTextViewMsg: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val editTextHost = EditText(this)
        editTextHost.hint = "Enter host ip"

        val editTextPort = EditText(this)
        editTextPort.hint = "Enter host port"

        val btnConnect = Button(this)
        btnConnect.text = "Connect tp host"

        mTextViewMsg = TextView(this)
        mTextViewMsg.text = "-----"

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
        rootLayout.addView(mTextViewMsg,-1,-2)

        setContentView(rootLayout)
    }


    private fun connectToHost(
        host: String,
        port: Int
    ) {
        Thread {
            val socket = Socket(host,port)
            runOnUiThread {
                addMessage("Connected to $host")
            }



        }.start()
    }

    private fun addMessage(
        text: String
    ) {
        mTextViewMsg.text = mTextViewMsg.text.toString() + text
    }
}