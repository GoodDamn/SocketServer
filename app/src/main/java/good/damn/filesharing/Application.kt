package good.damn.filesharing

import android.content.ContentResolver
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.servers.SSLServer
import good.damn.filesharing.servers.TCPServer
import java.io.InputStream
import java.net.ServerSocket
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

class Application
: android.app.Application() {

    companion object {

        lateinit var ASSETS: AssetManager
        lateinit var RESOURCES: Resources
        lateinit var CONTENT_RESOLVER: ContentResolver

        val BYTE_ORDER = ByteOrder
            .nativeOrder()
        val BUFFER_MB = ByteArray(1024*1024)
        val CHARSET = Charset.forName("UTF-8")
        val CHARSET_ASCII = Charset.forName("US-ASCII")
        var SERVER: TCPServer? = null
        var SERVER_SSL: SSLServer? = null

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

    override fun onCreate() {
        val context = applicationContext

        ASSETS = context
            .assets

        RESOURCES = context
            .resources

        CONTENT_RESOLVER = context
            .contentResolver

        super.onCreate()
    }

}