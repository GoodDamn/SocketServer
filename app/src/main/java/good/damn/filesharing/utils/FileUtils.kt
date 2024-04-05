package good.damn.filesharing.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class FileUtils {

    companion object {
        private val TAG = "FileUtils"

        fun read(
            uri: Uri?,
            context: Context
        ): ByteArray? {
            if (uri == null) {
                return null
            }

            val inp = context.contentResolver
                .openInputStream(uri) ?: return null

            val data = NetworkUtils
                .readBytes(inp)

            inp.close()

            return data
        }

        fun fromDoc(
            fileName: String
        ): ByteArray? {
            val docPath = getDocumentsFolder()
                .path

            val file = File("$docPath/$fileName")

            if (!file.exists()) {
                return null
            }

            val inps = FileInputStream(file)

            val b = NetworkUtils
                .readBytes(inps)

            inps.close()

            return b
        }

        fun writeToDoc(
            fileName: String,
            data: ByteArray,
            offset: Int = 0
        ): String? {

            val docPath = getDocumentsFolder()
                .path

            val file = File("$docPath/$fileName")

            try {
                if (!file.exists() && file.createNewFile()) {
                    Log.d(TAG, "writeToDoc: $file is created")
                }

                val fos = FileOutputStream(file)
                fos.write(data, offset, data.size - offset)
                fos.close()
            } catch (e: IOException) {
                Log.d(TAG, "writeToDoc: EXCEPTION ${e.message}")
                return "${e.message} for ${file.path}"
            }
            return null
        }

        fun getDocumentsFolder(): File {
            val dir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS
            )

            val subDir = File(dir, "Shared")

            if (!subDir.exists() && subDir.mkdir()) {
                Log.d(TAG, "writeToDoc: dir $subDir is created")
            }

            return subDir
        }
    }

}