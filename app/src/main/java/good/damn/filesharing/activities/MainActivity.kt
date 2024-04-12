package good.damn.filesharing.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity
: AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        val context = this
        val btnTcpServer = Button(
            context
        )

        val btnUdpServer = Button(
            context
        )

        val layout = LinearLayout(
            context
        )

        layout.orientation = LinearLayout.VERTICAL

        btnTcpServer.text = "TCP Server"
        btnUdpServer.text = "UDP Server"

        btnTcpServer.setOnClickListener(
            this::onClickBtnTcp
        )

        btnUdpServer.setOnClickListener(
            this::onClickBtnUdp
        )

        layout.addView(
            btnTcpServer,
            -1,
            -2
        )

        layout.addView(
            btnUdpServer,
            -1,
            -2
        )

        setContentView(
            layout
        )
    }


    private fun onClickBtnTcp(
        view: View
    ) {
        startActivity(
            Intent(
                this,
                ServerActivity::class.java
            )
        )
    }

    private fun onClickBtnUdp(
        view: View
    ) {
        startActivity(
            Intent(
                this,
                UDPServerActivity::class.java
            )
        )
    }

}