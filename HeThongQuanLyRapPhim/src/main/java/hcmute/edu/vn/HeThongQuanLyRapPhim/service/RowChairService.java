package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DayGhe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;

import java.util.List;

public interface RowChairService {
    List<DayGhe> getAllRowChairByIdRoom(int idPhongChieuPhim);
    DayGhe getRowChairById(int id);
    void updateRowChair(PhongChieuPhim phongChieuPhim, int soLuongDoi, int soLuongVip, int soLuongThuong);
    PhongChieuPhim getRoomById(int idPhongChieuPhim);
}