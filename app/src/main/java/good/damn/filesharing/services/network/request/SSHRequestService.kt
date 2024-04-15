package good.damn.filesharing.services.network.request

import good.damn.filesharing.shareProtocol.ShareMethod

class SSHRequestService {

    private val mRequests: HashMap<
        ShareMethod,
        ((ByteArray) -> ByteArray)
        > = HashMap()

    init {

    }
}