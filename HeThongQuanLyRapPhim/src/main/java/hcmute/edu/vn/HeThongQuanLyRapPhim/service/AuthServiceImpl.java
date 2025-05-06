package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TKDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.DoiTuongSuDungRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.TKDoiTuongSuDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private DoiTuongSuDungRepository doiTuongSuDungRepository;

    @Autowired
    private TKDoiTuongSuDungRepository tkDoiTuongSuDungRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void register(DoiTuongSuDung doiTuongSuDung, String tenDangNhap, String password, String confirmPassword) throws Exception {
        if (!password.equals(confirmPassword)) {
            throw new Exception("Mật khẩu xác nhận không khớp");
        }

        if (tkDoiTuongSuDungRepository.findByTenDangNhap(tenDangNhap).isPresent()) {
            throw new Exception("Tên đăng nhập đã được sử dụng");
        }

        doiTuongSuDungRepository.save(doiTuongSuDung);

        TKDoiTuongSuDung tkDoiTuongSuDung = new TKDoiTuongSuDung();
        tkDoiTuongSuDung.setTenDangNhap(tenDangNhap);
        tkDoiTuongSuDung.setMatKhau(passwordEncoder.encode(password));
        tkDoiTuongSuDung.setTrangThaiTaiKhoan(false);
        tkDoiTuongSuDung.setDoiTuongSuDung(doiTuongSuDung);

        TKDoiTuongSuDung savedTk = tkDoiTuongSuDungRepository.save(tkDoiTuongSuDung);

        sendVerificationEmail(doiTuongSuDung.getEmail(), savedTk.getIdTKDoiTuongSuDung());
    }

    @Override
    public String login(String username, String password) {
        TKDoiTuongSuDung tk = tkDoiTuongSuDungRepository.findByTenDangNhap(username)
                .orElse(null);
        if (tk == null) {
            return "Không tìm thấy tên đăng nhập";
        }
        if (!passwordEncoder.matches(password, tk.getMatKhau())) {
            return "Sai mật khẩu";
        }
        if (!tk.isTrangThaiTaiKhoan()) {
            return "Hãy kiểm tra Email của bạn để xác minh";
        }
        return "success";
    }

    @Override
    public void verifyAccount(int id) throws Exception {
        TKDoiTuongSuDung tk = tkDoiTuongSuDungRepository.findById(id)
                .orElseThrow(() -> new Exception("Tài khoản không tồn tại"));

        if (tk.isTrangThaiTaiKhoan()) {
            throw new Exception("Tài khoản đã được xác thực");
        }

        tk.setTrangThaiTaiKhoan(true);
        tkDoiTuongSuDungRepository.save(tk);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword, String confirmNewPassword) throws Exception {
        TKDoiTuongSuDung tk = tkDoiTuongSuDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new Exception("Tài khoản không tồn tại"));

        if (!passwordEncoder.matches(oldPassword, tk.getMatKhau())) {
            throw new Exception("Mật khẩu cũ không đúng");
        }

        if (!newPassword.equals(confirmNewPassword)) {
            throw new Exception("Mật khẩu mới xác nhận không khớp");
        }

        tk.setMatKhau(passwordEncoder.encode(newPassword));
        tkDoiTuongSuDungRepository.save(tk);
    }

    private void sendVerificationEmail(String email, int id) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        String verificationLink = "http://localhost:8080/auth/verify?id=" + id;
        context.setVariable("verificationLink", verificationLink);

        String emailContent = templateEngine.process("verify-email", context);

        helper.setTo(email);
        helper.setSubject("Xác thực tài khoản");
        helper.setText(emailContent, true);

        mailSender.send(message);
    }
}