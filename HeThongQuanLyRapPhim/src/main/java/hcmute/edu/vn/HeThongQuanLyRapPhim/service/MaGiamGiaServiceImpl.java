package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.MaGiamGiaRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class MaGiamGiaServiceImpl implements MaGiamGiaService {
    private MaGiamGiaRepository maGiamGiaRepository;
    @Autowired
    public MaGiamGiaServiceImpl(MaGiamGiaRepository maGiamGiaRepository) {
        this.maGiamGiaRepository = maGiamGiaRepository;
    }

    @Override
    public List<MaGiamGia> findAll() {
        return maGiamGiaRepository.findAll();
    }

    @Override
    public MaGiamGia findById(int theId) {
//        Kiểu dữ liệu Optional giúp xử lý trường hợp dữ liệu có thể null một cách an toàn.
        Optional<MaGiamGia> result = maGiamGiaRepository.findById(theId);
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
        return maGiamGiaRepository.save(maGiamGia);
    }

    @Override
    public void deleteById(int theId) {
        maGiamGiaRepository.deleteById(theId);
    }

    @Override
    public MaGiamGia updateCustomer(MaGiamGia maGiamGia) {
        return maGiamGiaRepository.save(maGiamGia);
    }
}
