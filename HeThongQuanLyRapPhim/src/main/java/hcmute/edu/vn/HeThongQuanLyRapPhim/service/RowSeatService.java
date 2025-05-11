package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DayGhe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;

import java.util.List;

public interface RowSeatService {
    List<DayGhe> findByPhongChieuPhim(PhongChieuPhim phongChieuPhim);
}
