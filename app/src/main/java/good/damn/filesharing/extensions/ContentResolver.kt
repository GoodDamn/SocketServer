package good.damn.filesharing.extensions

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns

inline fun ContentResolver.extractFileName(
    uri: Uri
) = query(
    uri,
    null,
    null,
    null,
    null
)?.run {
    val nameIndex = getColumnIndex(
        OpenableColumns.DISPLAY_NAME
    )
    moveToFirst()
    val fileName = getString(
        nameIndex
    )
    close()
    return@run fileName
}