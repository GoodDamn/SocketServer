package good.damn.filesharing.callbacks

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import good.damn.filesharing.utils.FileUtils

class ActivityResultCopyToDoc(
    private val context: Context
): ActivityResultCallback<Uri?> {

    override fun onActivityResult(
        uri: Uri?
    ) {
        val data = FileUtils
            .read(uri,
                context
            )

        if (data == null) {
            Toast.makeText(
                context,
                "Something went wrong",
                Toast.LENGTH_SHORT
            ).show()
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

        FileUtils.writeToDoc(
            fileName,
            data,
            0
        )

        Toast.makeText(
            context,
            "FILE IS COPIED $fileName",
            Toast.LENGTH_SHORT
        ).show()
    }

}