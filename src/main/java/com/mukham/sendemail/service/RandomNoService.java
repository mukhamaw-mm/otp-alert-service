package com.mukham.sendemail.service;

import com.mukham.sendemail.model.Mail;
import com.mukham.sendemail.repository.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RandomNoService {
    @Autowired
    MailRepository mailRep;
    @Value("${app.username}")
    String userName;
    @Value("${app.password}")
    String password;

    public String getUserEmail(String email) {
//        Random rnd = new Random();
        String otp = new DecimalFormat("000000").format(new Random().nextInt(999999));

        // this will convert any number sequence into 6 character.
        System.out.println("random number:" + otp);
        String to = "sample@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, password);
                    }
                });

        // Get the Session object.// and pass username and password

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(userName));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("This is OTP!");
            // Now set the actual message
            message.setText("This is your OTP code is " + otp);

            System.out.println("sending...");

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

            Mail mail1 = new Mail();
            mail1.setOtp(otp);
            mail1.setCreated_date(new Date());
            mailRep.save(mail1);


        } catch (Exception mex) {
            mex.printStackTrace();
            return "fail";

        }

        return "success";

    }

    public String getOtp(String otp) throws ParseException {
        Date dd1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String str = sdf.format(dd1);
        Date d1 = sdf.parse(str);

        Calendar c1 = Calendar.getInstance();//similar as new date
        c1.setTime(d1);
        c1.add(Calendar.HOUR_OF_DAY, 0);
//        c.set(Calendar.HOUR, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);

        System.out.println(c1);
        Timestamp t1 = new Timestamp(c1.getTimeInMillis());
        System.out.println(t1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d1);
        c2.set(Calendar.HOUR, 23);
        c2.set(Calendar.MINUTE, 59);
        c2.set(Calendar.SECOND, 59);
        Timestamp t2 = new Timestamp(c2.getTimeInMillis());
        System.out.println(t2);


        Mail mail = mailRep.findByOtp(otp, t1, t2);

        if (mail == null)
            return "Wrong OTP";
        else {
            Date dbDate = mail.getCreated_date();
            Date nowDate = new Date();
            try {

                //in milliseconds
                long diff = nowDate.getTime() - dbDate.getTime();
                long diffMinutes = diff / (60 * 1000) % 60;
                System.out.print(diffMinutes + " minutes.");
                if (diffMinutes < 3)
                    return "valid";
                else
                    return "invalid";

            } catch (Exception e) {
                e.printStackTrace();
            }


            return "Successfully login";
        }

    }
}
