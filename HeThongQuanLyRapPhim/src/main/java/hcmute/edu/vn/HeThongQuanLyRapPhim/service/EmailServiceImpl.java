package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoaDon;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;
    
    @Override
    public void guiHoaDonQuaEmail(String toEmail, HoaDon hoaDon) {
        try {
            Context context = new Context();
            context.setVariable("hoaDon", hoaDon);

            // Render nội dung HTML từ template
            String htmlContent = templateEngine.process("EmailHoaDonThanhCong.html", context);

            // Tạo email MIME
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("Hóa đơn thanh toán - Rạp chiếu phim");
            helper.setText(htmlContent, true); // true -> gửi email dạng HTML

            // Gửi email
            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể gửi email: " + e.getMessage());
        }
    }
}
