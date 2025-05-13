package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ChienDichGiamGia;

import java.util.List;

public interface DiscountCampaignService {
    List<ChienDichGiamGia> findAll();

    ChienDichGiamGia findById(int theId);

    ChienDichGiamGia save(ChienDichGiamGia chienDichGiamGia);

    void deleteById(int theId);
}
