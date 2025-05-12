package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;

import java.util.List;

public interface MaGiamGiaService {
    List<MaGiamGia> findAll();

    MaGiamGia findById(int theId);

    MaGiamGia save(MaGiamGia maGiamGia);

    void deleteById(int theId);
    MaGiamGia updateCustomer(MaGiamGia maGiamGia);
}
