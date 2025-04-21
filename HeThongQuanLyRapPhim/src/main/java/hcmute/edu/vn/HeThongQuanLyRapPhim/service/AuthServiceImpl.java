package hcmute.edu.vn.HeThongQuanLyRapPhim.service;


import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TKDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.LoaiDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.DoiTuongSuDungRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.TKDoiTuongSuDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private DoiTuongSuDungRepository doiTuongSuDungRepository;

    @Autowired
    private TKDoiTuongSuDungRepository tkDoiTuongSuDungRepository;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public String register(String hoTen, String email, String soDienThoai, LocalDateTime ngaySinh,
                           String tenDangNhap, String matKhau) throws MessagingException {
        if (doiTuongSuDungRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã được sử dụng!");
        }
        if (tkDoiTuongSuDungRepository.existsByTenDangNhap(tenDangNhap)) {
            throw new RuntimeException("Tên đăng nhập đã được sử dụng!");
        }

        DoiTuongSuDung doiTuongSuDung = new DoiTuongSuDung(
                hoTen, email, ngaySinh, null, LoaiDoiTuongSuDung.KHACH_HANG, soDienThoai
        );
        doiTuongSuDung = doiTuongSuDungRepository.save(doiTuongSuDung);

        String token = UUID.randomUUID().toString();
        TKDoiTuongSuDung tkDoiTuongSuDung = new TKDoiTuongSuDung(
                tenDangNhap, matKhau, false, doiTuongSuDung
        );
        tkDoiTuongSuDung = tkDoiTuongSuDungRepository.save(tkDoiTuongSuDung);

        sendVerificationEmail(email, token, tkDoiTuongSuDung.getIdTKDoiTuongSuDung());
        return "Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản.";
    }

    private void sendVerificationEmail(String email, String token, int userId) throws MessagingException {
        String verificationLink = "http://localhost:8080/api/auth/verify?token=" + token + "&id=" + userId;

        Context context = new Context();
        context.setVariable("verificationLink", verificationLink);

        String emailContent = templateEngine.process("verificationEmail", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("Xác thực tài khoản của bạn");
        helper.setText(emailContent, true);

        mailSender.send(message);
    }

    @Override
    public String verifyAccount(String token, int id) {
        TKDoiTuongSuDung tk = tkDoiTuongSuDungRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        if (!tk.isTrangThaiTaiKhoan()) {
            tk.setTrangThaiTaiKhoan(true);
            tkDoiTuongSuDungRepository.save(tk);
            return "Xác thực tài khoản thành công!";
        } else {
            return "Tài khoản đã được xác thực trước đó.";
        }
    }

    @Override
    public String login(String tenDangNhap, String matKhau) {
        TKDoiTuongSuDung tk = tkDoiTuongSuDungRepository.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() -> new RuntimeException("Tên đăng nhập không tồn tại!"));

        if (!tk.getMatKhau().equals(matKhau)) {
            throw new RuntimeException("Mật khẩu không đúng!");
        }

        if (!tk.isTrangThaiTaiKhoan()) {
            throw new RuntimeException("Tài khoản chưa được xác thực! Vui lòng kiểm tra email.");
        }

        return "Đăng nhập thành công!";
    }
}