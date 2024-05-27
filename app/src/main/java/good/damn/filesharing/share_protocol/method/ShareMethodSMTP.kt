package good.damn.filesharing.share_protocol.method

import good.damn.filesharing.Application
import good.damn.filesharing.services.network.smtp.SMTPService
import good.damn.filesharing.utils.ResponseUtils
import java.io.File
import java.io.OutputStream

class ShareMethodSMTP
: ShareMethodStream(
    "mail".toByteArray(
        Application.CHARSET_ASCII
    )
){

    companion object {
        private const val TAG = "ShareMethodSMTP"
    }

    private val mSmtpService = SMTPService()

    override fun responseStream(
        out: OutputStream,
        request: ByteArray,
        argsCount: Int,
        argsPosition: Int
    ) {

        val email: String
        val subject: String
        val body: String

        if (argsCount < 3) {
            ResponseUtils.responseMessageIdStream(
                out,
                "Not enough arguments. At least 3 (email, subject, body)"
            )
            return
        }

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

        mSmtpService.send(
            email,
            subject,
            body
        )

        return ResponseUtils.responseMessageIdStream(
            out,
            "Email sent"
        )
    }

}