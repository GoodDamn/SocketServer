package good.damn.filesharing.utils

import good.damn.filesharing.Application

class AssetUtils {

    companion object {

        fun loadString(
            path: String
        ): String {
            val inp = Application.ASSETS
                .open(path)

            val b = FileUtils
                .readBytes(inp)
            inp.close()

            return String(
                b,
                Application.CHARSET
            )
        }

    }

}