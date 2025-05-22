package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.DiscountCampaignRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ChienDichGiamGia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class DiscountCampaignServiceImpl implements DiscountCampaignService {
    private final DiscountCampaignRepository discountCampaignRepository;

    @Autowired
    public DiscountCampaignServiceImpl(DiscountCampaignRepository discountCampaignRepository) {
        this.discountCampaignRepository = discountCampaignRepository;
    }

    @Override
    public List<ChienDichGiamGia> findAll() {
        return discountCampaignRepository.findAll();
    }

    @Override
    public ChienDichGiamGia findById(int theId) {
        return discountCampaignRepository.findById(theId).orElse(null);
    }

    @Override
    public ChienDichGiamGia insert(String tenChienDich, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, MaGiamGia maGiamGia, int soLuongMaGiamGia) {
        ChienDichGiamGia chienDichGiamGia = new ChienDichGiamGia();
        chienDichGiamGia.setTenChienDich(tenChienDich);
        chienDichGiamGia.setNgayBatDauChienDich(ngayBatDau);
        chienDichGiamGia.setNgayKetThucChienDich(ngayKetThuc);
        chienDichGiamGia.addMaGiamGia(maGiamGia, soLuongMaGiamGia, chienDichGiamGia);

        return discountCampaignRepository.save(chienDichGiamGia);
    }

    @Override
    public ChienDichGiamGia update(int id, String tenChienDich, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc) {
        ChienDichGiamGia chienDichGiamGia = findById(id);

        // Gán giá trị mới
        chienDichGiamGia.setTenChienDich(tenChienDich);
        chienDichGiamGia.setNgayBatDauChienDich(ngayBatDau);
        chienDichGiamGia.setNgayKetThucChienDich(ngayKetThuc);

        return discountCampaignRepository.save(chienDichGiamGia);
    }


    @Override
    public boolean deleteById(int theId) {
        ChienDichGiamGia chienDichGiamGia = findById(theId);
        if (chienDichGiamGia != null) {
            discountCampaignRepository.deleteById(theId);
            return true;
        }
        return false;
    }
}
