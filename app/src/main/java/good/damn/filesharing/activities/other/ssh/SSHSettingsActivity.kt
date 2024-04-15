package good.damn.filesharing.activities.other.ssh

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.Application
import good.damn.filesharing.utils.FileUtils
import good.damn.filesharing.utils.SSHUtils

class SSHSettingsActivity
: AppCompatActivity() {

    private var mEditTextAuth: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = this

        val btnCreateUser = Button(
            context
        )

        mEditTextAuth = EditText(
            context
        )

        val layout = LinearLayout(
            context
        )

        layout.orientation = LinearLayout
            .VERTICAL

        btnCreateUser.setOnClickListener(
            this::onClickBtnCreateUser
        )

        layout.addView(
            mEditTextAuth,
            -1,
            -2
        )

        layout.addView(
            btnCreateUser,
            -1,
            -2
        )

        setContentView(
            layout
        )

    }

    private fun onClickBtnCreateUser(
        view: View
    ) {
        val credentials = mEditTextAuth?.text.toString()

        val folder = FileUtils.getUserFolder(
            credentials
        )

        if (credentials.isEmpty()) {
            Application.showMessage(
                "Empty credentials",
                this
            )
            return
        }

        if (folder.exists()) {
            Application.showMessage(
                "User exists",
                this
            )
            return
        }

        SSHUtils.createUser(
            credentials
        )

        Application.showMessage(
            "User has been created!",
            this
        )

    }

}