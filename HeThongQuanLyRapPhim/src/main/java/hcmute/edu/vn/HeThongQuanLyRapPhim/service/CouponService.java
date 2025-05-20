package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;

import java.util.List;

public interface CouponService {
    List<MaGiamGia> findAllMaGiamGia();
    MaGiamGia getMaGiamGia(int idMaGiamGia, int idKhachHang);
}
