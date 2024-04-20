package good.damn.filesharing.share_protocol.method

import android.util.Log
import good.damn.filesharing.Application
import good.damn.filesharing.services.network.smtp.SMTPService
import good.damn.filesharing.utils.ResponseUtils
import java.io.File

class ShareMethodSMTP
: ShareMethod(
    "mail".toByteArray(
        Application.CHARSET_ASCII
    )
){

    companion object {
        private const val TAG = "ShareMethodSMTP"
    }

    private val mSmtpService = SMTPService()

    override fun response(
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int,
        userFolder: File
    ): ByteArray {

        var email = ""
        var subject = ""
        var body = ""

        if (argsCount >= 3) {
            var pos = argsPosition
            val emailLen = request[pos]
                .toInt()
            pos++
            email = String(
                request,
                pos,
                emailLen,
                Application.CHARSET_ASCII
            )

            pos += emailLen

            val subjectLen = request[pos]
                .toInt()

            pos++
            subject = String(
                request,
                pos,
                subjectLen,
                Application.CHARSET_ASCII
            )

            pos += subjectLen

            val bodyLen = request[pos]
                .toInt()

            pos++
            body = String(
                request,
                pos,
                bodyLen,
                Application.CHARSET_ASCII
            )

        } else {
            return ResponseUtils.responseMessageId(
                "Not enough arguments. At least 3 (email, subject, body)"
            )
        }

        if (argsCount >= 4) {
            // With attachment
        }

        mSmtpService.send(
            email,
            subject,
            body
        )

        return ResponseUtils.responseMessageId(
            "Email sent"
        )
    }

}