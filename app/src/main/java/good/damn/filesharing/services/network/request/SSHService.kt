package good.damn.filesharing.services.network.request

import good.damn.filesharing.Application
import good.damn.filesharing.utils.FileUtils
import good.damn.filesharing.shareProtocol.method.ShareMethod
import good.damn.filesharing.shareProtocol.method.ssh.ShareMethodMakeDir
import good.damn.filesharing.shareProtocol.ssh.SSHAuth
import good.damn.filesharing.shareProtocol.ssh.SSHRequest

class SSHService {

    private val mRequests: HashMap<
        ShareMethod,
        (ByteArray) -> ByteArray
        > = HashMap()

    init {

        mRequests.set(ShareMethodMakeDir()) {
            val req = SSHRequest(
                it
            )
            req.decode()

            return@set ByteArray(0)
        }
    }

}