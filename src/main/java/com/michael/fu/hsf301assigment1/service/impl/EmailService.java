package com.michael.fu.hsf301assigment1.service.impl;

import com.michael.fu.hsf301assigment1.service.BaseService;
import com.michael.fu.hsf301assigment1.service.interfaces.NotificationService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
// Class chiu trach nhiem viec gui mail. toi implement NotificationService
// tuan thu quy tac O-Open Principle trong SOLID
public class EmailService extends BaseService implements NotificationService {

    // Tao Logger de xu ly log
    // Logger duoc xay dung theo Factory Pattern trong design pattern

    // Tạo Bean emailSender (JavaMailSender) trong Spring Context.
    // Sau đó, Spring tự động tiêm (Injection) vào lớp EmailService.
    // Chúng ta không cần tự khởi tạo đối tượng trong lớp này.
    // Điều này tuân thủ nguyên tắc Single Responsibility trong SOLID.
    // Sử dụng Dependency Injection (DI) thông qua IoC Container của Spring.

    private final JavaMailSender emailSender;

    // Tiêm giá trị từ file cấu hình application.yml
    // vào biến mailUsername. Cụ thể, lấy giá trị của thuộc tính "spring.mail.username".
    // Annotation @Value được dùng để inject giá trị đơn lẻ (primitive, String, v.v.)
    // từ Property file vào các biến trong Spring Component.
    @Value("${spring.mail.username}")
    private String mailUsername;
    @Value("${app.verification.base-uri}")
    private String verificationBaseUri;


    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }
    @Override
    public void sendVerification(String email, String verificationToken) {
        String subject = "Verification Email";
        String path = verificationBaseUri;
        String messages = "Click the link below to verify your email address: \n";
        sendEmail(subject, email, verificationToken, path, messages);
        logger.info("Verification Email has sent to  {}", email);
    }

    @Override
    public void sendResetPasswordEmail(String email, String resetToken) {

        String subject = "Resert Password Email";
        String path = "req/reset-password";
        String messages = "Click the link below to reset your password: \n";
        sendEmail(subject, email, resetToken, path, messages);
        logger.info("Reset Password Email has sent to  {}", email);
    }


    private void sendEmail(String subject, String email, String token, String path, String messages) {
        try {
            String actionUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(path)
                    .queryParam("token", token)
                    .toUriString();
            String emailContent = """
                    <div style="font-family: Arial, Helvetica, sans-serif; max-width: 600px; padding: 20px; border-radius: 8px; background-color: #f9f9f9; text-align: center;">
                     <h2 style="color: #333;"> %s </h2>
                       <p style="font-size: 16px; color: #555;">%s</p>
                               <a href="%s" style="display: inline-block; margin: 20px 0; padding: 10px 20px; font-size: 16px; color: #fff; background-color: #007bff; text-decoration: none; border-radius: 5px;">Proceed</a>
                               <p style="font-size: 14px; color: #777;">Or copy and paste this link into your browser:</p>
                               <p style="font-size: 14px; color: #007bff;">%s</p>
                               <p style="font-size: 12px; color: #aaa;">This is an automated message. Please do not reply.</p>
                           </div>
                    """.formatted(subject, messages, actionUrl, actionUrl);

            MimeMessage mineMessage = emailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mineMessage, true, "UTF-8");

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(mailUsername);
            mimeMessageHelper.setText(emailContent, true);
            emailSender.send(mineMessage);
            logger.info("Email sent to {}", email);
        } catch (Exception e) {

            logger.error("Error sending email: {}", e.getMessage());


        }
    }

}
