package good.damn.filesharing.activities.other.opengl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.views.LevelEditorView

class LevelEditorActivity
: AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        setContentView(
            LevelEditorView(
                this
            )
        )

    }

}