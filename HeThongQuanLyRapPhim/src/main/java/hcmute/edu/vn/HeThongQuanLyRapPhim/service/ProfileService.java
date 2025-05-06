package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;

public interface ProfileService {
    DoiTuongSuDung updateProfile(DoiTuongSuDung newProfile);
    DoiTuongSuDung getDoiTuongSuDung(Integer id);
}
