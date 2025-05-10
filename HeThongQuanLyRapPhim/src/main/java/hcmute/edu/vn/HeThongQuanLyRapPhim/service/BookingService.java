package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HinhThucChieu;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.SuatChieu;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    SuatChieu findById(int id);
    List<SuatChieu> getSuatChieuByPhimNgayChieuAndHinhThuc(Phim phim, LocalDate ngayChieu, HinhThucChieu hinhThucChieu);
}
