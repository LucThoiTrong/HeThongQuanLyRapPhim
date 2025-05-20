package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;

public interface AuthService {
    boolean checkIsAccountVerified(String username);
    DoiTuongSuDung login(String username, String password);
    boolean checkUsername(String username);
    boolean checkEmail(String email);
    boolean register(DoiTuongSuDung doiTuongSuDung, String tenDangNhap, String password);
    boolean verifyAccount(int idTaiKhoan);
    boolean sendMailForgotPassword(String email);
    void resetPassword(int id,String newPassword);
}