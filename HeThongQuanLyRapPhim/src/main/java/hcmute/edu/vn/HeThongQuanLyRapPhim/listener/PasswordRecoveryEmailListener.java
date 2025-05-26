package hcmute.edu.vn.HeThongQuanLyRapPhim.listener;

import hcmute.edu.vn.HeThongQuanLyRapPhim.event.AppEvent;
import hcmute.edu.vn.HeThongQuanLyRapPhim.event.PasswordRecoveryRequestedEvent;

import hcmute.edu.vn.HeThongQuanLyRapPhim.observer.EventListener;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Component
public class PasswordRecoveryEmailListener implements EventListener {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public PasswordRecoveryEmailListener(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void update(AppEvent appEvent) {
        if (appEvent instanceof PasswordRecoveryRequestedEvent event) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                Context context = new Context();
                String verificationLink = "https://phimhay.azurewebsites.net/reset-password?id=" + event.id();
                context.setVariable("verificationLink", verificationLink);

                String emailContent = templateEngine.process("EmailResetPassword", context);

                helper.setTo(event.email());
                helper.setSubject("Khôi phục mật khẩu");
                helper.setText(emailContent, true);

                mailSender.send(message);
            } catch (MessagingException e) {
                System.err.println("Lỗi khi gửi email khôi phục mật khẩu (Hybrid): " + e.getMessage());
            }
        }
    }
}