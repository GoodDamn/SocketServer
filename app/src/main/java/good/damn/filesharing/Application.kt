package good.damn.filesharing

import android.content.Context
import android.widget.Toast
import java.nio.Buffer
import java.nio.charset.Charset
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

class Application
: android.app.Application() {

    companion object {
        val BUFFER_MB = ByteArray(1024*1024)
        val CHARSET = Charset.forName("UTF-8")
        val CHARSET_ASCII = Charset.forName("US-ASCII")

        lateinit var SSL: SSLContext

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
        super.onCreate()

        val password = "123456"
            .toCharArray()

        val keyStore = KeyStore.getInstance("JKS")
        keyStore.load(
            resources.openRawResource(
                R.raw.multi
            ),
            password
        )

        val trustStore = KeyStore.getInstance("JKS")
        trustStore.load(
            resources.openRawResource(
                R.raw.multi_trust
            ),
            password
        )

        val keyManager = KeyManagerFactory.getInstance(
            KeyManagerFactory.getDefaultAlgorithm()
        )

        keyManager.init(
            keyStore,
            password
        )

        val trustManager = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )

        trustManager.init(
            trustStore
        )

        SSL = SSLContext.getInstance(
            "TLS"
        )

        SSL.init(
            keyManager.keyManagers,
            trustManager.trustManagers,
            null
        )
    }

}