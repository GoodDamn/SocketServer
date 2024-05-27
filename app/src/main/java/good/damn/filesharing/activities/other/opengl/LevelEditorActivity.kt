package good.damn.filesharing.activities.other.opengl

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import good.damn.filesharing.Application
import good.damn.filesharing.controllers.launchers.ContentLauncher
import good.damn.filesharing.views.LevelEditorView

class LevelEditorActivity
: AppCompatActivity(),
ActivityResultCallback<Uri?>{

    private lateinit var mLevelEditorView: LevelEditorView
    private lateinit var mContentLauncher: ContentLauncher

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        mContentLauncher = ContentLauncher(
            this,
            this
        )

        mLevelEditorView = LevelEditorView(
            this
        )

        setContentView(
            mLevelEditorView
        )
    }

    fun loadFromUserDisk(
        mime: String
    ) {
        mContentLauncher.launch(
            mime
        )
    }

    override fun onActivityResult(
        result: Uri?
    ) {
        if (result == null) {
            return
        }

        mLevelEditorView.onLoadFromUserDisk(
            contentResolver.openInputStream(
                result
            )
        )
    }

}