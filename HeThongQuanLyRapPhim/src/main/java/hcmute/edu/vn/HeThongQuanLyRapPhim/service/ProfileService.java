package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TKDoiTuongSuDung;

public interface ProfileService {
    DoiTuongSuDung updateProfile(DoiTuongSuDung newProfile, int idUser);
    DoiTuongSuDung getProfile(int idUser);
    boolean checkPassword(String password, int idUser);
    TKDoiTuongSuDung updatePassword(String newPassword, int idUser);
}
