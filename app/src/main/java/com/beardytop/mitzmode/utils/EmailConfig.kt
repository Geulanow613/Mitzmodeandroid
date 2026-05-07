package com.beardytop.mitzmode.utils

import android.content.Context
import android.util.Log
import com.beardytop.mitzmode.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object EmailConfig {
    private val senderEmail get() = BuildConfig.EMAIL_SENDER
    private val appPassword get() = BuildConfig.EMAIL_APP_PASSWORD
    private val defaultRecipient get() = BuildConfig.EMAIL_RECIPIENT
    
    suspend fun sendEmail(
        subject: String,
        body: String,
        recipientEmail: String = defaultRecipient
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (
                    senderEmail == "YOUR_EMAIL_HERE@gmail.com" ||
                    appPassword == "YOUR_APP_PASSWORD_HERE" ||
                    recipientEmail == "YOUR_EMAIL_HERE@gmail.com"
                ) {
                    Log.w("EmailConfig", "Email credentials not configured; skipping send.")
                    return@withContext false
                }

                val props = Properties().apply {
                    put("mail.smtp.host", "smtp.gmail.com")
                    put("mail.smtp.port", "587")
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.starttls.enable", "true")
                    put("mail.smtp.ssl.protocols", "TLSv1.2")
                    put("mail.smtp.ssl.ciphersuites", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256")
                }

                val session = Session.getInstance(props, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(senderEmail, appPassword)
                    }
                })

                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(senderEmail))
                    setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail))
                    setSubject(subject)
                    setText(body)
                }

                Transport.send(message)
                Log.d("EmailConfig", "Email sent successfully")
                true
            } catch (e: Exception) {
                Log.e("EmailConfig", "Failed to send email", e)
                false
            }
        }
    }

    suspend fun sendMitzvahSubmission(
        context: Context,
        mitzvahText: String,
        submitterName: String
    ): Boolean {
        val subject = "New Mitzvah Submission - Mitz Mode App"
        val body = """
            New mitzvah submission from the Mitz Mode app:
            
            Submitted by: $submitterName
            Mitzvah text: $mitzvahText
            
            Submitted on: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())}
            
            Please review and consider adding to the mitzvah database.
        """.trimIndent()
        
        return sendEmail(subject, body)
    }
} 