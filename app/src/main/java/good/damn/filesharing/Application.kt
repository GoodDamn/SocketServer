package good.damn.filesharing

import android.content.Context
import android.content.res.Resources
import android.widget.Toast
import good.damn.filesharing.servers.SSLServer
import good.damn.filesharing.servers.TCPServer
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
        val BYTE_ORDER = ByteOrder
            .nativeOrder()
        val BUFFER_MB = ByteArray(1024*1024)
        val CHARSET = Charset.forName("UTF-8")
        val CHARSET_ASCII = Charset.forName("US-ASCII")
        var SERVER: TCPServer? = null
        var SERVER_SSL: SSLServer? = null

        fun createSSLContext(
            resources: Resources
        ): SSLContext {
/*
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

            val SSL = SSLContext.getInstance(
                "TLS"
            )

            SSL.init(
                keyManager.keyManagers,
                trustManager.trustManagers,
                null
            )*/

            return SSLContext.getDefault()
        }

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