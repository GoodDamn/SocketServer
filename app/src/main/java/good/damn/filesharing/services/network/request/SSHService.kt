package good.damn.filesharing.services.network.request

import android.util.Log
import good.damn.filesharing.Application
import good.damn.filesharing.shareProtocol.interfaces.Responsible
import good.damn.filesharing.utils.FileUtils
import good.damn.filesharing.shareProtocol.method.ShareMethod
import good.damn.filesharing.shareProtocol.method.ssh.ShareMethodMakeDir
import good.damn.filesharing.shareProtocol.ssh.SSHAuth
import good.damn.filesharing.shareProtocol.ssh.SSHRequest
import java.net.InetAddress
import java.util.Hashtable

class SSHService {

    private val mRequests: Array<Responsible> = arrayOf(
        ShareMethodMakeDir()
    )

    companion object {
        private const val TAG = "SSHService"

        fun responseMessage(
            msg: String
        ): ByteArray {
            val data = msg
                .toByteArray(
                    Application.CHARSET_ASCII
                )

            return byteArrayOf(
                data.size.toByte()
            ) + data
        }
    }

    fun makeResponse(
        auth: SSHAuth,
        request: ByteArray,
    ): ByteArray {

        val offset = auth.contentOffset

        val argsCount = request[offset]
            .toInt() - 1

        val cmdLen = request[offset+1]
            .toInt()

        val cmdPos = offset + 2

        val index = ShareMethod(request,cmdPos,cmdLen)
            .hashCode() % mRequests.size

        if (index >= mRequests.size) {
            return responseMessage(
                "Hash calculating error $index"
            )
        }

        return mRequests[
            index
        ].response(
            request,
            argsCount,
            cmdPos+cmdLen,
            FileUtils.getUserFolder(
                auth.user
            )
        )
    }
}