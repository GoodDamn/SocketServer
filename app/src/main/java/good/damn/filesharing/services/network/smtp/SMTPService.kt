package good.damn.filesharing.services.network.smtp

import android.util.Log
import java.util.*
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class SMTPService {

    companion object {
        private const val TAG = "SMTPService"

        private const val EMAIL_COMMON = "vuzion@yandex.ru"
        private const val PASSWORD = "zaqwsxEdC1"
    }

    private val mProperties = Properties()
    private val mFromInetAddress = InternetAddress(
        EMAIL_COMMON
    )

    init {
        mProperties["mail.smtp.host"] = "smtp.yandex.ru";
        mProperties["mail.smtp.socketFactory.port"] = "465";
        mProperties["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory";
        mProperties["mail.smtp.auth"] = "true";
        mProperties["mail.smtp.port"] = "465";
    }

    fun send(
        to: String,
        subject: String,
        body: String
    ) {
        Thread {
            val session = Session.getDefaultInstance(
                mProperties,
                SMTPAuth(
                    EMAIL_COMMON,
                    PASSWORD
                )
            )

            try {

                val msg = MimeMessage(session)
                msg.setFrom(mFromInetAddress)

                msg.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
                )

                msg.subject = subject

                msg.setText(
                    body
                )

                Transport.send(
                    msg
                )
            } catch (e: Exception) {
                Log.d(TAG, "onCreate: EXCEPTION: ${e.message}")
            }
        }.start()
    }

}