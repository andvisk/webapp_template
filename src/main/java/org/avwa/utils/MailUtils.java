package org.avwa.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailUtils {

    private static Logger log = LoggerFactory.getLogger(MailUtils.class);

    public static boolean sendEmail(Session session, String toEmail, String subject, String body, String fromEmail) {
        MimeMessage msg = new MimeMessage(session);

        try {

            msg.setFrom(new InternetAddress(fromEmail, fromEmail));

            msg.setReplyTo(InternetAddress.parse(fromEmail, false));

            msg.setSubject(subject, "UTF-8");

            msg.setContent(body,"text/html; charset=utf-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            System.out.println("Message is ready");
            Transport.send(msg);

            return true;
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}
