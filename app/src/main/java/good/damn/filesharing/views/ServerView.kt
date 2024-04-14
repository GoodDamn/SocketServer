package good.damn.filesharing.views

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import good.damn.clientsocket.services.network.HotspotServiceCompat
import good.damn.filesharing.servers.BaseServer
import good.damn.filesharing.controllers.msgrs.Messenger
import good.damn.filesharing.listeners.network.service.HotspotServiceListener

class ServerView<DELEGATE>(
    private val mServer: BaseServer<DELEGATE>,
    context: Context
): LinearLayout(context),
    HotspotServiceListener {

    private val msgr = Messenger()

    private val mTextViewIP: TextView

    init {
        orientation = VERTICAL

        val btnStartServer = Button(
            context
        )

        val btnDropServer = Button(
            context
        )

        val textViewMsg = TextView(
            context
        )

        mTextViewIP = TextView(
            context
        )

        btnStartServer.text = "Start server"
        btnDropServer.text = "Drop server"
        textViewMsg.text = "----"

        mTextViewIP.textSize = 18f
        textViewMsg.textSize = 18f

        textViewMsg.movementMethod = ScrollingMovementMethod()
        textViewMsg.isVerticalScrollBarEnabled = true
        textViewMsg.isHorizontalScrollBarEnabled = false

        btnStartServer.setOnClickListener(
            this::onClickBtnStart
        )

        btnDropServer.setOnClickListener(
            this::onClickBtnStop
        )

        msgr.setTextView(
            textViewMsg
        )

        addView(
            mTextViewIP,
            -1,
            -2
        )

        addView(
            btnStartServer,
            -1,
            -2
        )

        addView(
            btnDropServer,
            -1,
            -2
        )

        addView(
            textViewMsg,
            -1,
            -1
        )

        val hotspotService = HotspotServiceCompat(
            context
        )

        hotspotService.delegate = this
        hotspotService.start()

    }

    override fun onGetHotspotIP(
        addressList: String
    ) {
        mTextViewIP.text = "Host: $addressList\nPort:"
    }

    private fun onClickBtnStart(
        view: View
    ) {
        mServer.start()
    }

    private fun onClickBtnStop(
        view: View
    ) {
        mServer.stop()
    }

    fun addMessage(
        msg: String
    ) {
        msgr.addMessage(
            msg
        )
    }
}