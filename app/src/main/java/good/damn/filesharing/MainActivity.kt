package good.damn.filesharing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import good.damn.filesharing.activities.ClientActivity
import good.damn.filesharing.activities.ServerActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val btnServer = Button(this)
        btnServer.text = "Create server"

        val btnClient = Button(this)
        btnClient.text = "Connect to a server"

        btnServer.setOnClickListener {
            startActivity(ServerActivity::class.java)
        }

        btnClient.setOnClickListener {
            startActivity(ClientActivity::class.java)
        }

        val rootLayout = LinearLayout(this)
        rootLayout.gravity = Gravity.CENTER
        rootLayout.orientation = LinearLayout.VERTICAL
        rootLayout.addView(btnServer,-2,-2)
        rootLayout.addView(btnClient,-2,-2)

        setContentView(rootLayout)
    }

    private fun startActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        super.startActivity(intent)
    }
}