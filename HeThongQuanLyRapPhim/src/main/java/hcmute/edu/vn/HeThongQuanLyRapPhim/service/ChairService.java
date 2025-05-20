package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Ghe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;

import java.util.List;

public interface ChairService {
    Ghe getChairById(int id);
    Ghe updateChair(int id, Ghe ghe);
    PhongChieuPhim getPhongChieuPhimById(int idPhongChieuPhim);
    void capNhatTrangThaiGhe(String danhSachGheDuocChon, boolean trangThaiGhe);
}
