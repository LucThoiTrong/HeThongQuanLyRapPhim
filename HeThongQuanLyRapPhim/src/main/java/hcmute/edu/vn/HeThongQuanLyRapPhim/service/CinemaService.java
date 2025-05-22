package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;

import java.util.List;

public interface CinemaService {
    // Chưa có rạp
    List<DoiTuongSuDung> getNhanVienChuaCoRap();
    // Chhưa có rạp + nhân viên đang chịu trách nhiệm quản lý
    List<DoiTuongSuDung> getDanhSachNhanVien(RapPhim rapPhim);
    List<RapPhim> isCinemaWithoutManager();
    List<RapPhim> getAllCinemas();
    RapPhim getCinemaById(int id);
    RapPhim createCinema(RapPhim rapPhim);
    RapPhim updateCinema(int id, RapPhim rapPhim);
    boolean deleteCinema(int id);
    RapPhim findCinemaByName(String tenRapPhim);
}
