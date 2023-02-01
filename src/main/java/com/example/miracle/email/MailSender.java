package com.example.miracle.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailSender {

    @Value("${spring.mail.username}")
    private String username;

    private final JavaMailSender mailSender;

    @Autowired
    public MailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


//    public void send(String emailTo, String subject, String message) {
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//
//        mailMessage.setFrom(username);
//        mailMessage.setTo(emailTo);
//        mailMessage.setSubject(subject);
//        mailMessage.setText(message);
//
//        mailSender.send(mailMessage);
//    }

    public void send(String emailTo, String subject, String message) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

            messageHelper.setFrom(username);
            messageHelper.setTo(emailTo);
            messageHelper.setSubject(subject);
            messageHelper.setText(message, true);

//            mimeMessage.setContent(messageHelper, "text/html; charset=utf-8");


        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }

        mailSender.send(mimeMessage);

//        message.setFrom(new InternetAddress(SENDER_EMAIL_ADDRESS, "Admin"));
//        message.addRecipient(Message.RecipientType.TO,
//                new InternetAddress(toAddress, "user");
//
//        message.setSubject(subject,"UTF-8");
//
//        Multipart mp = new MimeMultipart();
//        MimeBodyPart htmlPart = new MimeBodyPart();
//        htmlPart.setContent(message, "text/html");
//        mp.addBodyPart(htmlPart);
//        message.setContent(mp);
//        Transport.send(message);

    }
}
