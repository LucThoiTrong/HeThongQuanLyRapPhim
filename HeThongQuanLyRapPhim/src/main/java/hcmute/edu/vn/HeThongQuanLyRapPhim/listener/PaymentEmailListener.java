package hcmute.edu.vn.HeThongQuanLyRapPhim.listener;

import hcmute.edu.vn.HeThongQuanLyRapPhim.event.InvoiceGeneratedEvent;
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
public class PaymentEmailListener {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public PaymentEmailListener(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @EventListener
    @Async
    public void handleInvoiceGenerated(InvoiceGeneratedEvent event) throws MessagingException {
        org.thymeleaf.context.Context context = new Context();
        context.setVariable("hoaDon", event.getHoaDon());

        // Render nội dung HTML từ template
        String htmlContent = templateEngine.process("EmailInvoiceSuccess", context);

        // Tạo email MIME
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(event.getEmail());
        helper.setSubject("Hóa đơn thanh toán - Rạp chiếu phim");
        helper.setText(htmlContent, true); // true -> gửi email dạng HTML

        // Gửi email
        mailSender.send(message);
    }
}
