package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ChienDichGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;

import java.time.LocalDateTime;
import java.util.List;

public interface DiscountCampaignService {
    List<ChienDichGiamGia> findAll();
    ChienDichGiamGia findById(int theId);
    ChienDichGiamGia insert(String tenChienDich, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, MaGiamGia maGiamGia, int soLuongMaGiamGia);
    ChienDichGiamGia update(int id, String tenChienDich, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc);
    boolean deleteById(int theId);
}