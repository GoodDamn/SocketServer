package good.damn.filesharing.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.views.TrafficView

class MainActivity
: AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        val context = this

        val layout = LinearLayout(
            context
        )
        layout.orientation = LinearLayout.VERTICAL

        createServerButton(
            "TCP Server",
            TCPServerActivity::class.java,
            layout
        )

        createServerButton(
            "UDP Server",
            UDPServerActivity::class.java,
            layout
        )

        createServerButton(
            "SSH Server",
            SSHServerActivity::class.java,
            layout
        )

        layout.addView(
            TrafficView(context),
            -1,
            -1
        )

        setContentView(
            layout
        )
    }

    private fun createServerButton(
        text: String,
        activityClick: Class<*>,
        layout: LinearLayout
    ) {
        val context = layout.context
        val btnServer = Button(
            context
        )

        btnServer.text = text

        btnServer.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    activityClick
                )
            )
        }

        layout.addView(
            btnServer,
            -1,
            -2
        )
    }

}