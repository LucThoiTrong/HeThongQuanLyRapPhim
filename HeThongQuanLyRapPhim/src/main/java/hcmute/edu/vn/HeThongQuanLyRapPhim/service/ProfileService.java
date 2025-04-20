package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TKDoiTuongSuDung;

public interface ProfileService {
    DoiTuongSuDung updateProfile(DoiTuongSuDung newProfile);
    DoiTuongSuDung getDoiTuongSuDung(Integer id);
}
