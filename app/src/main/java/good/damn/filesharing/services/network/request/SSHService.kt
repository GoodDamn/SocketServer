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
    }

    fun makeResponse(
        request: ByteArray,
        offset: Int = 0
    ): ByteArray {

        val index = ShareMethod(request,offset)
            .hashCode() % mRequests.size

        if (index >= mRequests.size) {
            return responseMessage(
                "Hash calculating error $index"
            )
        }

        return mRequests[
            index
        ].response(request, offset)
    }

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