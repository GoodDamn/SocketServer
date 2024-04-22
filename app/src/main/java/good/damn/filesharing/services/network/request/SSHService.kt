package good.damn.filesharing.services.network.request

import good.damn.filesharing.share_protocol.interfaces.Responsible
import good.damn.filesharing.utils.FileUtils
import good.damn.filesharing.share_protocol.method.ShareMethod
import good.damn.filesharing.share_protocol.method.ssh.ShareMethodMakeDir
import good.damn.filesharing.share_protocol.ssh.SSHAuth
import good.damn.filesharing.utils.ResponseUtils

class SSHService {

    private val mRequests: Array<Responsible> = arrayOf(
        ShareMethodMakeDir()
    )

    companion object {
        private const val TAG = "SSHService"
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
            return ResponseUtils.responseMessageId(
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