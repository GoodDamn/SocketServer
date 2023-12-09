package good.damn.filesharing.factory

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import good.damn.filesharing.models.Connectable
import good.damn.filesharing.models.Messenger
import good.damn.filesharing.models.Server

class ViewFactory {

    companion object {
        fun createClientView(
            msgr: Messenger,
            context: Context,
            connectable: Connectable
        ): View {
            val editTextHost = EditText(context)
            editTextHost.hint = "Enter host ip"

            val editTextPort = EditText(context)
            editTextPort.hint = "Enter host port"

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
                connectable.connectToHost(
                    editTextHost.text.toString(),
                    editTextPort.text.toString().toInt()
                )
            }

            val rootLayout = LinearLayout(context)
            rootLayout.gravity = Gravity.CENTER
            rootLayout.orientation = LinearLayout.VERTICAL

            rootLayout.addView(editTextHost, -1, -2)
            rootLayout.addView(editTextPort, -1, -2)
            rootLayout.addView(btnConnect, -1, -2)
            rootLayout.addView(textViewMsg, -1, -2)

            return rootLayout
        }
    }
}