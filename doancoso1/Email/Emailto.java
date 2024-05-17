package com.example.doancoso1.Email;

import com.example.doancoso1.RL.Login;

import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;

public class Emailto {
    private static String luutru_teencode;
    private static Login login;

    public static void sendEmail(String to) {
        String username = "nguyenvanhoang19092005@gmail.com"; // Địa chỉ email của bạn
        String password = "imgljkbasyhsxlwr"; // Mật khẩu ứng dụng

        // Tạo mã xác nhận ngẫu nhiên
        String maTeenCode = taoteenCode();
        luutru_teencode = maTeenCode;

        // Cấu hình thông tin máy chủ email
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");




        // Tạo phiên làm việc
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Tạo đối tượng Message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Mã Xác Nhận");

            // Nội dung email
            String emailContent = "Mã xác nhận của bạn là: " + maTeenCode;
            message.setText(emailContent);

            // Gửi email
            Transport.send(message);
            JOptionPane.showMessageDialog(login, "Đã gửi mã xác nhận đến " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static String getVerificationCode() {
        return luutru_teencode;
    }

    private static String taoteenCode() {
        // Tạo mã xác nhận ngẫu nhiên 6 chữ số
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
