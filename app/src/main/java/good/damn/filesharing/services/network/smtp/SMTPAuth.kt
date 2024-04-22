package good.damn.filesharing.services.network.smtp

import javax.mail.PasswordAuthentication
import javax.mail.Authenticator

class SMTPAuth(
    private val email: String,
    private val password: String
): Authenticator() {

    override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(
            email,
            password
        )
    }

}