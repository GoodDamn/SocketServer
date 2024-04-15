package good.damn.filesharing

import android.content.Context
import android.widget.Toast
import java.nio.Buffer
import java.nio.charset.Charset

class Application
: android.app.Application() {

    companion object {
        val BUFFER_MB = ByteArray(1024*1024)
        val CHARSET = Charset.forName("UTF-8")
        val CHARSET_ASCII = Charset.forName("US-ASCII")

        fun showMessage(
            msg: String,
            context: Context
        ) {
            Toast.makeText(
                context,
                msg,
                Toast.LENGTH_SHORT
            ).show()
        }

    }

}