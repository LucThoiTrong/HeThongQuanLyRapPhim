package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;

import java.util.List;

public interface UserService {
    DoiTuongSuDung getDoiTuongSuDungById(int id);
    List<DoiTuongSuDung> getNhanVienChuaCoRap();
}
