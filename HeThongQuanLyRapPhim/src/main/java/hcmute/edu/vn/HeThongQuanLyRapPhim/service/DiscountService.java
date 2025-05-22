package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ChienDichGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;

import java.util.List;

public interface DiscountService {
    List<MaGiamGia> findAll();
    List<ChienDichGiamGia> findAllChienDichGiamGia();
    MaGiamGia findById(int theId);
    MaGiamGia insert(MaGiamGia theMaGiamGia);
    MaGiamGia update(int id, MaGiamGia theMaGiamGia);
    boolean deleteById(int theId);
}
