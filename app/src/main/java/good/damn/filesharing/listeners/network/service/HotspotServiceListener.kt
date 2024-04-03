package good.damn.filesharing.listeners.network.service

import android.net.LinkAddress

interface HotspotServiceListener {

    fun onGetHotspotIP(
        addressList: String
    )

}