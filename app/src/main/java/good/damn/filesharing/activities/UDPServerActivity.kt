package good.damn.filesharing.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.controllers.UDPServer

class UDPServerActivity
: AppCompatActivity() {

    private val mUdpServer = UDPServer(
        8080,
        ByteArray(300)
    )

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        val context = this

        val btnStart = Button(
            context
        )

        val btnStop = Button(
            context
        )

        val layout = LinearLayout(
            context
        )

        btnStart.text = "Start Server"
        btnStop.text = "Stop Server"

        btnStart.setOnClickListener(
            this::onClickBtnStart
        )

        btnStop.setOnClickListener(
            this::onClickBtnStop
        )

        layout.orientation = LinearLayout
            .VERTICAL

        layout.addView(
            btnStart,
            -1,
            -2
        )

        layout.addView(
            btnStop,
            -1,
            -2
        )

        setContentView(
            layout
        )
    }

    private fun onClickBtnStart(
        view: View
    ) {
        mUdpServer.start()
    }

    private fun onClickBtnStop(
        view: View
    ) {
        mUdpServer.stop()
    }
}