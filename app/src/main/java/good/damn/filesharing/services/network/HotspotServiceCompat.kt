package good.damn.clientsocket.services.network

import android.content.Context
import android.os.Build
import good.damn.filesharing.listeners.network.service.HotspotServiceListener
import good.damn.clientsocket.services.BaseService
import good.damn.filesharing.services.network.HotspotService

class HotspotServiceCompat(
    private val context: Context
) {

    var delegate: HotspotServiceListener? = null
        set(value) {
            mService.delegate = value
            field = value
        }

    private val mService: BaseService<HotspotServiceListener>

    init {
        mService = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            HotspotServiceApi30(context)
            else HotspotService(context)
    }

    fun start() {
        mService.start()
    }
}