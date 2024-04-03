package good.damn.filesharing.manager.request

import android.util.Log
import good.damn.filesharing.manager.RequestManager
import good.damn.filesharing.utils.FileUtils
import java.nio.charset.Charset

class HTTPResponseManager {

    private val TAG = "HTTPResponseManager"

    private val CHARSET = Charset.forName("UTF-8")

    public fun execute(
        path: String,
    ): ByteArray {

        val data = FileUtils
            .fromDoc(
                path.ifEmpty { "welcome" }
            ) ?: return getHeaderError()

        if (path.contains(".")) {
            return getHeaderFile(
                data.size,
                path
            ).plus(data)
        }

        return getHeaderDocument(data.size)
            .plus(data)
    }

    private fun getHeaderFile(
        contentSize: Int,
        fileName: String
    ): ByteArray {
        return (
            "HTTP/1.0 200 OK\r\n" +
            "Content-Length: $contentSize\r\n" +
            "Content-Type: application/octet-stream;\r\n" +
            "Content-Disposition: inline; filename=\"$fileName\"\r\n\r\n"
        ).toByteArray(CHARSET)
    }

    private fun getHeaderError(): ByteArray {
        return (
            "HTTP/1.0 404 Not Found\r\n"+
            "Content-Length: 9\r\n"+
            "Content-Type: text/html; \r\n"+
            "Date: Mon, 29 Jan 2024 17:09:46 GMT\r\n\r\nNot found"
        ).toByteArray(CHARSET)
    }

    private fun getHeaderDocument(
        contentSize: Int
    ): ByteArray {
        return (
            "HTTP/1.0 200 OK\r\n" +
            "Content-Length: $contentSize\r\n" +
            "Content-Type: text/html; charset=UTF-8\r\n" +
            "Date: Mon, 29 Jan 2024 17:09:46 GMT\r\n\r\n"
        ).toByteArray(CHARSET)
    }
}