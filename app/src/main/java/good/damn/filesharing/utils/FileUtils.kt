package good.damn.filesharing.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import good.damn.filesharing.Application
import java.io.ByteArrayOutputStream
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

        fun readFile(
            file: File,
            buffer: ByteArray
        ): ByteArray {
            val fis = FileInputStream(file)
            val baos = ByteArrayOutputStream()
            var n: Int
            while(true) {
                n = fis.read(buffer)
                if (n == -1) {
                    break
                }
                baos.write(buffer,0,n)
            }
            val res = baos.toByteArray()
            baos.close()
            fis.close()

            return res
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

            return writeFile(
                file,
                data,
                offset
            )
        }

        private fun writeFile(
            file: File,
            data: ByteArray,
            offset: Int = 0
        ): String? {
            return try {
                if (!file.exists() && file.createNewFile()) {
                    Log.d(TAG, "writeFile: $file is created")
                }

                val fos = FileOutputStream(file)
                fos.write(data, offset, data.size - offset)
                fos.close()
                null
            } catch (e: IOException) {
                Log.d(TAG, "writeFile: EXCEPTION: ${e.message}")
                "${e.message} for ${file.path} WHICH EXISTING IS ${file.exists()}"
            }
        }

        fun getDocumentsFolder(): File {
            val dir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS
            )

            val subDir = File(dir, "ServerDir")

            if (!subDir.exists() && subDir.mkdir()) {
                Log.d(TAG, "writeToDoc: dir $subDir is created")
            }

            return subDir
        }

        fun hasUserRsa(
            user: String
        ): Boolean {
            return getUserRsaFile(
                user
            ).exists()
        }

        fun isUserFolderExists(
            user: String
        ): Boolean {
            return getUserFolder(user)
                .exists()
        }

        fun getUserRsaFile(
            user: String
        ): File {
            return getUserFolder(
                "$user/key"
            )
        }

        fun saveUserRsa(
            user: String,
            rsaKey: String
        ) {
            val folder = getUserFolder(
                user
            ).path

            val file = File("$folder/key")

            Log.d(TAG, "saveUserRsa: $file")
            
            writeFile(
                file,
                rsaKey.toByteArray(
                    Application.CHARSET_ASCII
                )
            )
        }

        fun getUsersRsa(
            buffer: ByteArray
        ): HashSet<String> {
            val hashSet = HashSet<String>()
            val users = getUsers() ?: return hashSet
            for (user in users) {
                val keyFile = File("${user.path}/key")
                if (keyFile.exists()) {
                    val keyData = readFile(
                        keyFile,
                        buffer
                    )
                    hashSet.add(String(
                        keyData,
                        Application.CHARSET_ASCII
                    ))
                }
            }
            return hashSet
        }

        fun getUsers(): Array<File>? {
            return File("${getDocumentsFolder()}/ssh")
                .listFiles()
        }

        fun getUserFolder(
            user: String
        ): File {
            return File("${getDocumentsFolder()}/ssh/${user}")
        }
    }

}