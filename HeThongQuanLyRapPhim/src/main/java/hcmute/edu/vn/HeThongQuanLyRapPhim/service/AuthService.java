package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;

public interface AuthService {
    void register(DoiTuongSuDung doiTuongSuDung, String tenDangNhap, String password) throws Exception;
    DoiTuongSuDung login(String username, String password);
    void verifyAccount(int id) throws Exception;
    void changePassword(String username, String oldPassword, String newPassword, String confirmNewPassword) throws Exception;
}