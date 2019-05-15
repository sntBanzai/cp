/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.service.mail;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author jerzy.malyszko
 */
@Service("mailSender")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MailSenderService {

    private final static String HOST_KEY = "mail.smtp.host";
    private static final String MAILSMTPPORT = "mail.smtp.port";
    private static final String MAILSMTPAUTH = "mail.smtp.auth";
    private static final String MAILSMTPSOCKET_FACTORYCLASS = "mail.smtp.socketFactory.class";
    private static final String MAILSMTPSOCKET_FACTORYPORT = "mail.smtp.socketFactory.port";
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";

    private Properties smtpProperties;

    public void sendMail(MailSenderInput input) throws IOException {

        Properties props = getConfigurationProps();

        Session session = setSession(props);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(input.getSenderEmailAddress()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(input.getRecipientEmailAddresses(), false));
            message.setSubject(input.getMailSubject());

            MimeMultipart content = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(input.getMailContent(), "text/html;charset=UTF-8");
            content.addBodyPart(textPart);

            for (Map.Entry<String,File>  att : input.getAttachments().entrySet()) {
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.attachFile(att.getValue());
                imagePart.setHeader("Content-ID", att.getKey());
                content.addBodyPart(imagePart);
            }

            message.setContent(content);
            useTransport(session, message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private Session setSession(Properties props) {
        Session session = null;
        if (Boolean.valueOf(props.getProperty(MAILSMTPAUTH))) {
            session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    Optional<String> username = Optional.ofNullable(props.getProperty(USERNAME));
                    Optional<String> password = Optional.ofNullable(props.getProperty(PASSWORD));
                    if (!username.isPresent() || !password.isPresent()) {
                        throw new RuntimeException("SMTP authentication parameters not specified in smtp.properties");
                    }
                    return new PasswordAuthentication(username.get(), password.get());
                }

            });
        } else {
            session = Session.getInstance(props);
        }
        return session;
    }

    private void useTransport(Session session, Message message) throws MessagingException, NoSuchProviderException {
        Transport transport = session.getTransport();
        transport.connect();
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    private Properties getConfigurationProps() {
        if (smtpProperties == null) {
            smtpProperties = new Properties();
            try {
                smtpProperties.load(getClass().getResourceAsStream("/smtp.properties"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return smtpProperties;
    }

}
