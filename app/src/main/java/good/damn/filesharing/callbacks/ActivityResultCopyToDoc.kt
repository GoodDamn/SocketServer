package good.damn.filesharing.callbacks

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import good.damn.filesharing.listeners.activityResult.ActivityResultCopyListener
import good.damn.filesharing.utils.FileUtils

class ActivityResultCopyToDoc(
    private val context: Context
): ActivityResultCallback<Uri?> {

    var delegate: ActivityResultCopyListener? = null

    override fun onActivityResult(
        uri: Uri?
    ) {
        val data = FileUtils
            .read(uri,
                context
            )

        if (data == null) {
            delegate?.onErrorCopyFile(
                "data == null"
            )
            return
        }

        val p = uri!!.path!!
        val t = "primary:"
        val filePath = p.substring(
            p.indexOf(t) + t.length
        )

        val nameIndex = filePath.lastIndexOf(
            "/"
        )

        val fileName = if (nameIndex == -1)
            filePath
        else filePath.substring(
            nameIndex + 1
        )

        val msg = FileUtils.writeToDoc(
            fileName,
            data
        )

        if (msg == null) {
            delegate?.onSuccessCopyFile(
                fileName
            )
            return
        }

        delegate?.onErrorCopyFile(
            msg
        )
    }

}