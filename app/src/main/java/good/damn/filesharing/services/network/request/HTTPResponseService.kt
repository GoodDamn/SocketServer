package good.damn.filesharing.services.network.request

import android.util.Log
import good.damn.filesharing.Application
import good.damn.filesharing.utils.FileUtils
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream

class HTTPResponseService {
    companion object {
        private const val TAG = "HTTPResponseManager"
        fun executeStream(
            out: OutputStream,
            path: String
        ) {
            val doc = FileUtils
                .getDocumentsFolder()

            Log.d(TAG, "executeStream: $path")
            val file = File("$doc/$path")
            val fileLen = file.length()

            if (file.isDirectory) {
                val content = generateHtmlListFiles(
                    file
                ).toByteArray(
                    Application.CHARSET
                )

                out.write(
                    getHeaderDocument(
                        content.size
                    )
                )

                out.write(
                    content
                )
                return
            }

            if (!file.exists() || fileLen == 0L) {
                out.write(
                    getHeaderError()
                )
                return
            }

            if (path.contains(".")) {
                out.write(
                    getHeaderFile(
                        fileLen.toInt(),
                        path
                    )
                )
                FileUtils.copyStream(
                    FileInputStream(
                        file
                    ),
                    out
                )
                return
            }

            out.write(
                getHeaderDocument(
                    fileLen.toInt()
                )
            )

            FileUtils.copyStream(
                FileInputStream(
                    file
                ),
                out
            )
        }
    }
}

private inline fun generateHtmlListFiles(
    dir: File
): String {
    val builder = StringBuilder()

    builder.append(
        "<html><ul>"
    )

    dir.listFiles()?.forEach {
        builder.append("<li>")
        builder.append("<a href=\"${it.name}\">")
        builder.append(it.name)
        builder.append("</a></li>")
    }

    builder.append(
        "</ul></html>"
    )

    return builder.toString()
}

private inline fun getHeaderFile(
    contentSize: Int,
    fileName: String
): ByteArray {
    return (
        "HTTP/1.0 200 OK\r\n" +
            "Content-Length: $contentSize\r\n" +
            "Content-Type: application/octet-stream;\r\n" +
            "Content-Disposition: inline; filename=\"$fileName\"\r\n\r\n"
        ).toByteArray(Application.CHARSET)
}

private inline fun getHeaderError(): ByteArray {
    return (
        "HTTP/1.0 404 Not Found\r\n"+
            "Content-Length: 9\r\n"+
            "Content-Type: text/html; \r\n"+
            "Date: Mon, 29 Jan 2024 17:09:46 GMT\r\n\r\nNot found"
        ).toByteArray(Application.CHARSET)
}

private inline fun getHeaderDocument(
    contentSize: Int
): ByteArray {
    return (
        "HTTP/1.0 200 OK\r\n" +
            "Content-Length: $contentSize\r\n" +
            "Content-Type: text/html; charset=UTF-8\r\n" +
            "Date: Mon, 29 Jan 2024 17:09:46 GMT\r\n\r\n"
        ).toByteArray(Application.CHARSET)
}