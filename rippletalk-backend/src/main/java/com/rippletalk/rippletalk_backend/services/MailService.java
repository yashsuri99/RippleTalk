package com.rippletalk.rippletalk_backend.services;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.rippletalk.rippletalk_backend.exceptions.EmailFailedToSendException;

import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

  private final Gmail gmail;

  public MailService(Gmail gmail) {
    this.gmail = gmail;
  }

  public void sendEmail(String toAddress, String subject, String content) throws Exception {
    Properties props = new Properties();

    Session session = Session.getInstance(props, null);

    MimeMessage email = new MimeMessage(session);

    try {
      email.setFrom(new InternetAddress("yashsuri1999@gmail.com"));
      email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(toAddress));
      email.setSubject(subject);
      email.setText(content);

      ByteArrayOutputStream buffer = new ByteArrayOutputStream();

      email.writeTo(buffer);

      byte[] rawMessageBytes = buffer.toByteArray();

      String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);

      Message message = new Message();
      message.setRaw(encodedEmail);

      message = gmail.users().messages().send("me", message).execute();
    } catch (Exception e) {
      throw new EmailFailedToSendException();
    }
  }

}
