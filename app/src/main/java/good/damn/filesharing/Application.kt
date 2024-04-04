package good.damn.filesharing

import java.nio.Buffer
import java.nio.charset.Charset

class Application
: android.app.Application() {

    companion object {
        val BUFFER_MB = ByteArray(1024*1024)
        val CHARSET = Charset.forName("UTF-8")
    }

}