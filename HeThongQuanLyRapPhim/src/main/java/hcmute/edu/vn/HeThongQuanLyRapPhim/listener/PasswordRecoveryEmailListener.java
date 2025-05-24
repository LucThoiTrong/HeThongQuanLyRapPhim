package hcmute.edu.vn.HeThongQuanLyRapPhim.listener;

import hcmute.edu.vn.HeThongQuanLyRapPhim.event.PasswordRecoveryRequestedEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
public class PasswordRecoveryEmailListener {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public PasswordRecoveryEmailListener(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @EventListener
    @Async
    public void handlePasswordRecoveryRequested(PasswordRecoveryRequestedEvent event) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        String verificationLink = "https://phimhay.azurewebsites.net/reset-password?id=" + event.getId();
        context.setVariable("verificationLink", verificationLink);

        String emailContent = templateEngine.process("EmailResetPassword", context);

        helper.setTo(event.getEmail());
        helper.setSubject("Khôi phục mật khẩu");
        helper.setText(emailContent, true);

        mailSender.send(message);
    }
}
