package hcmute.edu.vn.HeThongQuanLyRapPhim.listener;

import hcmute.edu.vn.HeThongQuanLyRapPhim.event.AppEvent;
import hcmute.edu.vn.HeThongQuanLyRapPhim.event.InvoiceGeneratedEvent;

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
public class PaymentEmailListener implements EventListener {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public PaymentEmailListener(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void update(AppEvent appEvent) {
        if (appEvent instanceof InvoiceGeneratedEvent(
                String email, hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoaDon hoaDon
        )) {
            try {
                Context context = new Context(); // Thymeleaf Context
                context.setVariable("hoaDon", hoaDon);

                String htmlContent = templateEngine.process("EmailInvoiceSuccess", context);

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setTo(email);
                helper.setSubject("Hóa đơn thanh toán - Rạp chiếu phim");
                helper.setText(htmlContent, true);

                mailSender.send(message);
            } catch (MessagingException e) {
                System.err.println("Lỗi khi gửi email hóa đơn (Hybrid): " + e.getMessage());
            }
        }
    }
}