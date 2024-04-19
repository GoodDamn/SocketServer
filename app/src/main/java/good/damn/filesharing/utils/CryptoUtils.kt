package good.damn.filesharing.utils

import android.util.Base64
import good.damn.filesharing.Application
import java.security.MessageDigest

class CryptoUtils {
    companion object {

        private val mDigestSha256 = MessageDigest
            .getInstance("SHA-256")

        fun sha256Base64(
            input: String
        ): String {
            val hash = sha256(
                input
            )

            return Base64.encodeToString(
                hash,
                Base64.URL_SAFE
            ).replace("\\s+".toRegex(), "")
        }

        fun sha256Base64(
            input: ByteArray,
            offset: Int = 0,
            len: Int = input.size - offset
        ): String {
            return Base64.encodeToString(
                input,
                offset,
                len,
                Base64.URL_SAFE
            ).replace("\\s+".toRegex(), "")
        }

        private fun sha256(
            input: String
        ): ByteArray {
            mDigestSha256.reset()
            return mDigestSha256.digest(
                input.toByteArray(
                    Application.CHARSET_ASCII
                )
            )
        }
    }
}