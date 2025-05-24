package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.event.PasswordRecoveryRequestedEvent;
import hcmute.edu.vn.HeThongQuanLyRapPhim.event.RegistrationInitiatedEvent;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TKDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.UserRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository doiTuongSuDungRepository;

    private final UserAccountRepository tkDoiTuongSuDungRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public AuthServiceImpl(UserRepository doiTuongSuDungRepository,
                           UserAccountRepository tkDoiTuongSuDungRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           ApplicationEventPublisher eventPublisher) {
        this.doiTuongSuDungRepository = doiTuongSuDungRepository;
        this.tkDoiTuongSuDungRepository = tkDoiTuongSuDungRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    // Check tài khoản đã được kích hoạt hay chưa
    @Override
    public boolean checkIsAccountVerified(String username) {
        TKDoiTuongSuDung tkDoiTuongSuDung = tkDoiTuongSuDungRepository.findByTenDangNhap(username);
        if (tkDoiTuongSuDung == null) {
            return false;
        }
        return tkDoiTuongSuDung.isTrangThaiTaiKhoan();
    }

    // Thực hiện đăng nhập
    @Override
    public DoiTuongSuDung login(String username, String password) {
        TKDoiTuongSuDung tk = tkDoiTuongSuDungRepository.findByTenDangNhap(username);
        if (tk == null || !passwordEncoder.matches(password, tk.getMatKhau())) {
            return null;
        }
        return tk.getDoiTuongSuDung();
    }

    // Check tên đăng nhập đã tồn tại trong hệ thống hay chưa
    @Override
    public boolean checkUsername(String username) {
        TKDoiTuongSuDung tkDoiTuongSuDung = tkDoiTuongSuDungRepository.findByTenDangNhap(username);
        return tkDoiTuongSuDung == null;
    }

    // Check email đã tồn tại trong hệ thống hay chưa
    @Override
    public boolean checkEmail(String email) {
        DoiTuongSuDung doiTuongSuDung = doiTuongSuDungRepository.findDoiTuongSuDungByEmail(email);
        return doiTuongSuDung == null;
    }

    // Thực hiện chức năng đăng ký
    @Override
    public boolean register(DoiTuongSuDung doiTuongSuDung, String tenDangNhap, String password)  {
        // Thực hiện lưu thông tin khách hàng
        doiTuongSuDungRepository.save(doiTuongSuDung);

        // Tạo tài khoản cho khách hàng
        TKDoiTuongSuDung tkDoiTuongSuDung = new TKDoiTuongSuDung(tenDangNhap, passwordEncoder.encode(password), false, doiTuongSuDung);

        // Thực hiện lưu tài khoản khách hàng
        TKDoiTuongSuDung result = tkDoiTuongSuDungRepository.save(tkDoiTuongSuDung);

        RegistrationInitiatedEvent registrationInitiatedEvent = new RegistrationInitiatedEvent(this, doiTuongSuDung.getEmail(), result.getIdTKDoiTuongSuDung());
        eventPublisher.publishEvent(registrationInitiatedEvent);

        return true;
    }

    // Thực hiện chức năng xác thực tài khoản
    @Override
    public boolean verifyAccount(int idTaiKhoan)  {
        TKDoiTuongSuDung tk = tkDoiTuongSuDungRepository.findById(idTaiKhoan).orElse(null);
        if (tk != null) {
            tk.setTrangThaiTaiKhoan(true);
            tkDoiTuongSuDungRepository.save(tk);
            return true;
        }
        return false;
    }

    // Thực hiện chức năng quên mật khẩu
    @Override
    public boolean sendMailForgotPassword(String email) {
        DoiTuongSuDung doiTuongSuDung = doiTuongSuDungRepository.findDoiTuongSuDungByEmail(email);
        if (doiTuongSuDung == null) {
            return false;
        }
        PasswordRecoveryRequestedEvent passwordRecoveryRequestedEvent = new PasswordRecoveryRequestedEvent(this, email, doiTuongSuDung.getTkDoiTuongSuDung().getIdTKDoiTuongSuDung());
        eventPublisher.publishEvent(passwordRecoveryRequestedEvent);
        return true;
    }


    @Override
    public void resetPassword(int id, String newPassword) {
        TKDoiTuongSuDung tk = tkDoiTuongSuDungRepository.findById(id).orElse(null);
        if (tk != null) {
            tk.setMatKhau(passwordEncoder.encode(newPassword));
            tkDoiTuongSuDungRepository.save(tk);
        }
    }
}