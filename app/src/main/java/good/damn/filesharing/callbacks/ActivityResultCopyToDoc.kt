package good.damn.filesharing.callbacks

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import good.damn.filesharing.extensions.extractFileName
import good.damn.filesharing.listeners.activityResult.ActivityResultCopyListener
import good.damn.filesharing.utils.FileUtils

class ActivityResultCopyToDoc(
    private val context: Context
): ActivityResultCallback<Uri?> {

    var delegate: ActivityResultCopyListener? = null

    override fun onActivityResult(
        uri: Uri?
    ) {
        if (uri == null) {
            delegate?.onErrorCopyFile(
                "uri == null"
            )
            return
        }

        val resolver = context.contentResolver
        val extractedFileName = resolver.extractFileName(
            uri
        )

        if (extractedFileName == null) {
            delegate?.onErrorCopyFile(
                "extracted file name == null"
            )
            return
        }

        val stream = resolver.openInputStream(
            uri
        )

        if (stream == null) {
            delegate?.onErrorCopyFile(
                "copy stream == null"
            )
            return
        }

        val msg = FileUtils.writeToDoc(
            extractedFileName,
            stream
        )

        if (msg == null) {
            delegate?.onSuccessCopyFile(
                extractedFileName
            )
            return
        }

        delegate?.onErrorCopyFile(
            msg
        )
    }

}