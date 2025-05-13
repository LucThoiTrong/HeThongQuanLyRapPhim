package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.DiscountCampaignRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ChienDichGiamGia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
//        Kiểu dữ liệu Optional giúp xử lý trường hợp dữ liệu có thể null một cách an toàn.
        Optional<ChienDichGiamGia> result = discountCampaignRepository.findById(theId);
        ChienDichGiamGia chienDichGiamGia = null;
        if (result.isPresent()) {
            chienDichGiamGia = result.get();
        }
        else {
            // we didn't find the employee
            throw new RuntimeException("Did not find combo bap nuoc id - " + theId);
        }

        return chienDichGiamGia;
    }

    @Override
    public ChienDichGiamGia save(ChienDichGiamGia chienDichGiamGia) {
        return discountCampaignRepository.save(chienDichGiamGia);
    }

    @Override
    public void deleteById(int theId) {
        discountCampaignRepository.deleteById(theId);
    }
}
