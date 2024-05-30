package good.damn.filesharing.services.network.request

import good.damn.filesharing.Application
import good.damn.filesharing.utils.FileUtils
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream

class HTTPResponseService {

    private val TAG = "HTTPResponseManager"

    companion object {
        fun executeStream(
            out: OutputStream,
            path: String
        ) {
            val doc = FileUtils
                .getDocumentsFolder()

            val file = File("$doc/$path")
            val fileLen = file.length()

            if (file.isDirectory || !file.exists() || fileLen == 0L) {
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

        private fun getHeaderFile(
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

        private fun getHeaderError(): ByteArray {
            return (
                "HTTP/1.0 404 Not Found\r\n"+
                    "Content-Length: 9\r\n"+
                    "Content-Type: text/html; \r\n"+
                    "Date: Mon, 29 Jan 2024 17:09:46 GMT\r\n\r\nNot found"
                ).toByteArray(Application.CHARSET)
        }

        private fun getHeaderDocument(
            contentSize: Int
        ): ByteArray {
            return (
                "HTTP/1.0 200 OK\r\n" +
                    "Content-Length: $contentSize\r\n" +
                    "Content-Type: text/html; charset=UTF-8\r\n" +
                    "Date: Mon, 29 Jan 2024 17:09:46 GMT\r\n\r\n"
                ).toByteArray(Application.CHARSET)
        }
    }
}