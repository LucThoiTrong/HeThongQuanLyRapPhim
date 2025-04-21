package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import jakarta.mail.MessagingException;
import java.time.LocalDateTime;

public interface AuthService {
    String register(String hoTen, String email, String soDienThoai, LocalDateTime ngaySinh,
                    String tenDangNhap, String matKhau) throws MessagingException;

    String verifyAccount(String token, int id);

    String login(String tenDangNhap, String matKhau);
}