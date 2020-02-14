/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brugerautorisation.server;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Sender en mail. Kræver JavaMail
 * @author j
 */
public class SendMail {

  public static void sendMail(String emne, String tekst, String modtagere) throws MessagingException {
    // Husk først at sænke sikkerheden på https://www.google.com/settings/security/lesssecureapps
    // Eller bruge en 'Genereret app-adgangskode' - se https://support.google.com/accounts/answer/185833?hl=da
    final String afsender = "jacob.nordfalk@gmail.com";
    System.out.println("sendMail " + emne + " " + modtagere + " " + tekst.replace('\n', ' '));
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    // FØLGENDE KRÆVER JavaMail-BIBLIOTEKET
    // fjern evt koden, da du ikke skal sende mail fra din PC (det gør serveren jo)

    Session session = Session.getInstance(props, new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        Path kodefil = Paths.get("gmail-adgangskode.txt");
        try {
          String adgangskode = new String(Files.readAllBytes(kodefil));
          return new PasswordAuthentication(afsender, adgangskode);
        } catch (IOException ex) {
          System.err.println("Du kan ikke sende mails før du har konfigurerer afsender (" + afsender + ") til lav sikkerhed:\nhttps://www.google.com/settings/security/lesssecureapps\nog og lagt adgangskoden i " + kodefil);
          ex.printStackTrace();
        }
        return null;
      }
    });
    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(afsender));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(modtagere));
    message.setRecipients(Message.RecipientType.CC, InternetAddress.parse("jacob.nordfalk@gmail.com"));
    message.setSubject(emne);
    message.setText(tekst);
    Transport.send(message);
  }

  public static void main(String[] args) throws MessagingException {
    sendMail("Test af Genereret app-adgangskode", "test", "jacno@dtu.dk");
  }
}
