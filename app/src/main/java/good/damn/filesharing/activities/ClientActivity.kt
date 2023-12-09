package good.damn.filesharing.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.models.Connectable
import good.damn.filesharing.models.Messenger
import good.damn.filesharing.models.views.ClientView
import java.net.Socket

class ClientActivity
    : AppCompatActivity() {

    private lateinit var mClientView: ClientView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mClientView = ClientView(this)
        setContentView(mClientView)
    }
}