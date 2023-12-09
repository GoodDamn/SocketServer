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
import good.damn.filesharing.factory.ViewFactory
import good.damn.filesharing.models.Connectable
import good.damn.filesharing.models.Messenger
import java.net.Socket
import java.nio.charset.Charset

class ClientActivity
    : AppCompatActivity(),
      Connectable {

    private val msgr = Messenger()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ViewFactory.createClientView(
            msgr,
            this,
            this
        ))
    }

    override fun onGetResponse(data: ByteArray) {
        msgr.addMessage(String(data,msgr.getCharset()))
    }

    override fun onConnected(
        socket: Socket
    ) {
        msgr.addMessage("Connected to ${socket.remoteSocketAddress}")
    }

}