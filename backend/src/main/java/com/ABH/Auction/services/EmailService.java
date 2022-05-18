package com.ABH.Auction.services;

import com.ABH.Auction.responses.MessageResponse;
import com.ABH.Auction.utility.EmailSender;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {

    private final JavaMailSender mailSender;
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Override
    @Async
    public void send(String to, String email) {
        MimeMessage mimeMessage = createMimeMessage(to, email, "Confirm your email");
        if(mimeMessage != null) {
            mailSender.send(mimeMessage);
        }
    }

    public void sendVerificationEmail(String email, String name, String link) {
        String content = "Dear  " + name + ",<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"" + link + "\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "ABH.";
        send(email, content);
    }

    public void sendPassRecoveryEmail(String email, String name, String link) {
        String content = "Dear  " + name + ",<br>"
                + "Please click the link below to change your password:<br>"
                + "<h3><a href=\"" + link + "\" target=\"_self\">RESET PASSWORD</a></h3>"
                + "Thank you,<br>"
                + "ABH.";
        send(email, content);
    }

    public String rateSellerTemplate(String name, String link) {
        return "Dear  " + name + ",<br>"
                + "Did your item find a way to you?<br>"
                + "<h3><a href=\"" + link + "\" target=\"_self\">REVIEW SELLER</a></h3>"
                + "If yes, let us know if you like it,<br>"
                + "ABH.";
    }

    @Async
    public void sendEmailWithSubject(String to, String body, String subject) {
        MimeMessage mimeMessage = createMimeMessage(to, body, subject);
        if(mimeMessage != null) {
            mailSender.send(mimeMessage);
        }
    }

    private MimeMessage createMimeMessage(String to, String body, String subject) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(body, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("abhauction@gmail.com");
        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            return null;
        }
        return mimeMessage;
    }

    @Async
    public MessageResponse sendFile(String fileName, String to, String subject,
                                    String text, String extension) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                    true, "utf-8");
            helper.addAttachment(fileName, new File(fileName));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("abhauction@gmail.com");
            MimeBodyPart attachPart = new MimeBodyPart();
            //suspicious
            attachPart.attachFile(new File(fileName).getAbsolutePath() + extension);
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text, "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachPart);
            mimeMessage.setContent(multipart);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            return new MessageResponse("Email not sent!", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new MessageResponse("Email sent!", true);
    }
}
