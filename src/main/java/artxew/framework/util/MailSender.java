package artxew.framework.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.web.multipart.MultipartFile;

import artxew.framework.environment.exception.DefinedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class MailSender {

    private static final String MAIL_LOG
            = "send mail: {} ->\n\tto: [ {} ]\n\tcc: [ {} ]\n\tbcc: [ {} ]";

    private static final String TEXT_HTML = "text/html";

    private static final String SEND_MAIL_ERROR = "send-mail-error";

    protected static String username;

    protected static String password;

    protected static String address;

    private static Session session;

    private MailSender() {
        throw new IllegalStateException("Utility class");
    }

    public static void send(
        String subject
        , String content
        , String to
    ) {
        send(subject, content, to, null, null);
    }

    public static void send(
        String subject
        , String content
        , String to
        , String cc
        , String bcc
    ) {
        log.info(MAIL_LOG, subject, to, cc, bcc);
        try {
            Message message = getMessage();

            message.setSubject(subject);
            message.setContent(content, TEXT_HTML);
            send(message, to, cc, bcc);
        } catch (MessagingException e) {
            throw new DefinedException(SEND_MAIL_ERROR, e);
        }
    }

    public static void send(
        String subject
        , String content
        , String to
        , String cc
        , String bcc
        , File ...fileList
    ) {
        log.info(MAIL_LOG, subject, to, cc, bcc);
        try {
            Message message = getMessage();

            message.setSubject(subject);
            if (fileList == null) {
                message.setContent(content, TEXT_HTML);
            } else {
                Multipart mp = new MimeMultipart();
                BodyPart bp = new MimeBodyPart();

                bp.setContent(content, TEXT_HTML);
                mp.addBodyPart(bp);
                for (File file : fileList) {
                    bp = new MimeBodyPart();
                    bp.setDataHandler(new DataHandler(new FileDataSource(file)));
                    bp.setFileName(file.getName());
                    mp.addBodyPart(bp);
                }
                message.setContent(mp);
            }
            send(message, to, cc, bcc);
        } catch (MessagingException e) {
            throw new DefinedException(SEND_MAIL_ERROR, e);
        }
    }

    public static void send(
        String subject
        , String content
        , String to
        , String cc
        , String bcc
        , MultipartFile ...fileList
    ) {
        log.info(MAIL_LOG, subject, to, cc, bcc);
        try {
            Message message = getMessage();

            message.setSubject(subject);
            if (fileList == null) {
                message.setContent(content, TEXT_HTML);
            } else {
                Multipart mp = new MimeMultipart();
                BodyPart bp = new MimeBodyPart();

                bp.setContent(content, TEXT_HTML);
                mp.addBodyPart(bp);
                for (MultipartFile file : fileList) {
                    bp = new MimeBodyPart();
                    bp.setDataHandler(new DataHandler(
                        new ByteArrayDataSource(file.getBytes(), file.getContentType())
                    ));
                    bp.setFileName(file.getOriginalFilename());
                    mp.addBodyPart(bp);
                }
                message.setContent(mp);
            }
            send(message, to, cc, bcc);
        } catch (MessagingException|IOException e) {
            throw new DefinedException(SEND_MAIL_ERROR, e);
        }
    }

    private static Message getMessage() {
        if (session == null) {
            Properties prop = new Properties();

            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
            prop.put("mail.smtp.starttls.enable", "true");
            session = Session.getInstance(
                prop
                , new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                }
            );
        }
        return new MimeMessage(session);
    }

    private static void send(
        Message message
        , String to
        , String cc
        , String bcc
    ) throws MessagingException {
        message.setFrom(
            new InternetAddress(address)
        );
        if (to != null) {
            message.setRecipients(
                RecipientType.TO
                , InternetAddress.parse(to)
            );
        }
        if (cc != null) {
            message.setRecipients(
                RecipientType.CC
                , InternetAddress.parse(cc)
            );
        }
        if (bcc != null) {
            message.setRecipients(
                RecipientType.BCC
                , InternetAddress.parse(bcc)
            );
        }
        Transport.send(message);
    }
}