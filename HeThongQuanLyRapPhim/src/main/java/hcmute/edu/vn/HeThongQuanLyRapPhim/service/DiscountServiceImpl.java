package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ChienDichGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.DiscountCampaignRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.DiscountRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final DiscountCampaignRepository discountCampaignRepository;

    @Autowired
    public DiscountServiceImpl(DiscountRepository discountRepository, DiscountCampaignRepository discountCampaignRepository) {
        this.discountRepository = discountRepository;
        this.discountCampaignRepository = discountCampaignRepository;
    }

    @Override
    public List<MaGiamGia> findAll() {
        return discountRepository.findAll();
    }

    @Override
    public List<ChienDichGiamGia> findAllChienDichGiamGia() {
        return discountCampaignRepository.findAll();
    }

    @Override
    public MaGiamGia findById(int theId) {
        return discountRepository.findById(theId).orElse(null);
    }

    @Override
    public MaGiamGia insert(MaGiamGia theMaGiamGia) {
        return discountRepository.save(theMaGiamGia);
    }

    @Override
    public MaGiamGia update(int id, MaGiamGia theMaGiamGia) {
        MaGiamGia maGiamGia = discountRepository.findById(id).orElse(null);
        if(maGiamGia != null) {
            return discountRepository.save(theMaGiamGia);
        }
        return null;
    }


    @Override
    public boolean deleteById(int theId) {
        MaGiamGia maGiamGia = discountRepository.findById(theId).orElse(null);
        if(maGiamGia != null) {
            discountRepository.deleteById(theId);
            return true;
        }
        return false;
    }
}
