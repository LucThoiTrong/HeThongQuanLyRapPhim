package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.DiscountRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class DiscountServiceImpl implements DiscountService {
    private DiscountRepository discountRepository;
    @Autowired
    public DiscountServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Override
    public List<MaGiamGia> findAll() {
        return discountRepository.findAll();
    }

    @Override
    public MaGiamGia findById(int theId) {
//        Kiểu dữ liệu Optional giúp xử lý trường hợp dữ liệu có thể null một cách an toàn.
        Optional<MaGiamGia> result = discountRepository.findById(theId);
        MaGiamGia maGiamGia = null;
        if (result.isPresent()) {
            maGiamGia = result.get();
        }
        else {
            throw new RuntimeException("Did not find combo bap nuoc id - " + theId);
        }

        return maGiamGia;
    }

    @Override
    public MaGiamGia save(MaGiamGia maGiamGia) {
        return discountRepository.save(maGiamGia);
    }

    @Override
    public void deleteById(int theId) {
        discountRepository.deleteById(theId);
    }

    @Override
    public MaGiamGia updateCustomer(MaGiamGia maGiamGia) {
        return discountRepository.save(maGiamGia);
    }
}
