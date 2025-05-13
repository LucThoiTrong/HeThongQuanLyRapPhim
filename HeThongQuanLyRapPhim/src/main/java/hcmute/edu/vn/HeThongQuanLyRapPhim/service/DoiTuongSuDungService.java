package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;

import java.util.List;

public interface DoiTuongSuDungService {
    List<DoiTuongSuDung> getAllDoiTuongSuDung();
    DoiTuongSuDung getDoiTuongSuDungById(int id);
    DoiTuongSuDung createDoiTuongSuDung(DoiTuongSuDung doiTuongSuDung);
    DoiTuongSuDung updateDoiTuongSuDung(DoiTuongSuDung doiTuongSuDung);
    boolean deleteDoiTuongSuDung(int id);
    List<DoiTuongSuDung> getNhanVienChuaCoRap();
}
