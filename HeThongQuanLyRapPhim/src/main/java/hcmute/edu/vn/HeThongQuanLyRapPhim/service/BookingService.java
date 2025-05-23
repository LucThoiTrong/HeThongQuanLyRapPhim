package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BookingService {
    Phim findPhimById(int id);
    List<ComboBapNuoc> findAllComboBapNuoc();
    SuatChieu findById(int id);
    Map<String, List<SuatChieu>> getShowtimesForCinema(LocalDate date, HinhThucChieu hinhThucChieu, Phim phim);
    List<DayGhe> findAllDayGhe(PhongChieuPhim phongChieuPhim);
    Map<Integer, Integer> extractComboSoLuong(Map<String, String> tatCaThamSo);
    boolean checkMaGiamGia(double tongSoTien, MaGiamGia maGiamGia);
    double tinhTienGiam(double tongHoaDon, MaGiamGia maGiamGia);
    DoiTuongSuDung findDoiTuongSuDungById(int id);
}