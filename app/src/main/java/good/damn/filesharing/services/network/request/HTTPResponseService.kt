package good.damn.filesharing.services.network.request

import good.damn.filesharing.Application
import good.damn.filesharing.utils.FileUtils
import java.io.OutputStream

class HTTPResponseService {

    private val TAG = "HTTPResponseManager"

    companion object {
        fun executeStream(
            out: OutputStream,
            path: String
        ) {
            val data = FileUtils
                .fromDoc(
                    path.ifEmpty { "welcome" }
                )

            if (data == null) {
                out.write(
                    getHeaderError()
                )
                return
            }

            if (path.contains(".")) {
                out.write(
                    getHeaderFile(
                        data.size,
                        path
                    )
                )
                out.write(data)
                return
            }

            out.write(
                getHeaderDocument(
                    data.size
                )
            )

            out.write(
                data
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