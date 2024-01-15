package artxew.framework.util;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import org.springframework.web.multipart.MultipartFile;
import artxew.framework.environment.exception.DefinedException;
import artxew.framework.environment.flowlog.FlowLogHolder;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;

/**
 * @author Artxe2
 */
public final class MailSender {
	private static Session session;
	private static final String TEXT_HTML = "text/html; charset=UTF-8";
	private static final String SEND_MAIL_ERROR = "send-mail-error";
	protected static String address;
	protected static String host;
	protected static String password;
	protected static String personal;
	protected static String port;
	protected static String username;

	/**
	 * @author Artxe2
	 */
	private MailSender() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * @author Artxe2
	 */
	public static void send(
		String subject
		, String content
		, String to
	) {
		send(subject, content, to, null, null);
	}

	/**
	 * @author Artxe2
	 */
	public static void send(
		String subject
		, String content
		, String to
		, String cc
		, String bcc
	) {
		try {
			Message message = getMessage();
			message.setSubject(subject);
			message.setContent(content, TEXT_HTML);
			send(message, to, cc, bcc);
		} catch (Exception e) {
			throw new DefinedException(SEND_MAIL_ERROR, e);
		}
	}

	/**
	 * @author Artxe2
	 */
	public static void send(
		String subject
		, String content
		, String to
		, String cc
		, String bcc
		, File ...fileList
	) {
		StringBuilder sb = new StringBuilder("MailSender.send(")
				.append(subject)
				.append(", ...String, ...File)");
		FlowLogHolder.touch(sb.toString());
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
				for (var file : fileList) {
					String fileName = new String(file.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
					bp = new MimeBodyPart();
					bp.setDataHandler(new DataHandler(new FileDataSource(file)));
					bp.setFileName(fileName);
					mp.addBodyPart(bp);
				}
				message.setContent(mp);
			}
			send(message, to, cc, bcc);
		} catch (Exception e) {
			throw new DefinedException(SEND_MAIL_ERROR, e);
		}
	}

	/**
	 * @author Artxe2
	 */
	public static void send(
		String subject
		, String content
		, String to
		, String cc
		, String bcc
		, MultipartFile ...fileList
	) {
		StringBuilder sb = new StringBuilder("MailSender.send(")
				.append(subject)
				.append(", ...String, ...MultipartFile)");
		FlowLogHolder.touch(sb.toString());
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
				for (var file : fileList) {
					@SuppressWarnings("null")
					String fileName = new String(file.getOriginalFilename().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
					bp = new MimeBodyPart();
					bp.setDataHandler(new DataHandler(new ByteArrayDataSource(file.getBytes(), file.getContentType())));
					bp.setFileName(fileName);
					mp.addBodyPart(bp);
				}
				message.setContent(mp);
			}
			send(message, to, cc, bcc);
		} catch (Exception e) {
			throw new DefinedException(SEND_MAIL_ERROR, e);
		}
	}

	/**
	 * @author Artxe2
	 */
	private static Message getMessage() {
		if (session == null) {
			Authenticator authenticator = new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			};
			Properties properties = new Properties();
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.host", host);
			properties.put("mail.smtp.port", port);
			properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
			properties.put("mail.smtp.ssl.checkserveridentity", "true");
			properties.put("mail.smtp.ssl.trust", host);
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.transport.protocol", "smtp");
			properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			session = Session.getInstance(properties, authenticator);
		}
		return new MimeMessage(session);
	}

	/**
	 * @author Artxe2
	 */
	private static void send(
		Message message
		, String to
		, String cc
		, String bcc
	) throws Exception {
		message.setFrom(new InternetAddress(address, personal, "UTF-8"));
		if (to != null && !to.isEmpty()) {
			message.setRecipients(RecipientType.TO, InternetAddress.parse(to));
		}
		if (cc != null && !cc.isEmpty()) {
			message.setRecipients(RecipientType.CC, InternetAddress.parse(cc));
		}
		if (bcc != null && !bcc.isEmpty()) {
			message.setRecipients(RecipientType.BCC, InternetAddress.parse(bcc));
		}
		Transport.send(message);
	}
}