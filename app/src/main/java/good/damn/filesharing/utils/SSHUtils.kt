package good.damn.filesharing.utils

import android.util.Log

class SSHUtils {
    companion object {
        private const val TAG = "SSHUtils"
        fun createUser(
            user: String
        ): Boolean {
            return FileUtils.getUserFolder(
                user
            ).mkdirs()
        }

    }
}