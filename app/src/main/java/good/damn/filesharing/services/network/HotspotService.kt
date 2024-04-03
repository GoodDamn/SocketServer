package good.damn.filesharing.services.network

import android.content.Context
import android.net.*
import android.net.wifi.WifiManager
import good.damn.clientsocket.services.BaseService
import good.damn.filesharing.listeners.network.service.HotspotServiceListener
import good.damn.filesharing.utils.ByteUtils
import java.nio.ByteOrder

@Deprecated("dhcpInfo of WifiManager class is deprecated")
class HotspotService(
    context: Context
): BaseService<HotspotServiceListener>(context) {

    companion object {
        private const val TAG = "HotspotService"
    }

    private val mWifiManager: WifiManager

    init {
        // may causes memory leak
        mWifiManager = context.applicationContext.getSystemService(
           Context.WIFI_SERVICE
        ) as WifiManager
    }

    override fun start() {

        val dhcp = mWifiManager.dhcpInfo
        val ipDhcp = dhcp.gateway

        if (ipDhcp == 0) {
            delegate?.onGetHotspotIP(
                "0.0.0.0"
            )
            return
        }

        val ip = ByteUtils.integer(
            if (ByteOrder.nativeOrder()
                    .equals(ByteOrder.LITTLE_ENDIAN)
            ) Integer.reverseBytes(ipDhcp)
            else ipDhcp
        )

        delegate?.onGetHotspotIP(
            "${ip[0]}.${ip[1]}.${ip[2]}.${ip[3]}"
        )
    }

}