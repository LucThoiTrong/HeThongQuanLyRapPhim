package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoaDon;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
    
    @Override
    public void guiHoaDonQuaEmail(String toEmail, HoaDon hoaDon){
        try {
            Context context = new Context();
            context.setVariable("hoaDon", hoaDon);

            // Render nội dung HTML từ template
            String htmlContent = templateEngine.process("EmailInvoiceSuccess", context);

            // Tạo email MIME
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("Hóa đơn thanh toán - Rạp chiếu phim");
            helper.setText(htmlContent, true); // true -> gửi email dạng HTML

            // Gửi email
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Không thể gửi email: " + e.getMessage());
        }
    }

    @Override
    public void sendVerificationEmail(String email, int id) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        String verificationLink = "https://phimhay.azurewebsites.net/auth/verify?id=" + id;
        context.setVariable("verificationLink", verificationLink);

        String emailContent = templateEngine.process("EmailVerify", context);

        helper.setTo(email);
        helper.setSubject("Xác thực tài khoản");
        helper.setText(emailContent, true);

        mailSender.send(message);
    }

    @Override
    public void sendResetPasswordEmail(String email, int id) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        String verificationLink = "https://phimhay.azurewebsites.net/reset-password?id=" + id;
        context.setVariable("verificationLink", verificationLink);

        String emailContent = templateEngine.process("EmailResetPassword", context);

        helper.setTo(email);
        helper.setSubject("Khôi phục mật khẩu");
        helper.setText(emailContent, true);

        mailSender.send(message);
    }
}
