package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.ChienDichMaGiamGiaRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ChienDichGiamGia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChienDichMaGiamGiaServiceImpl implements ChienDichMaGiamGiaService {
    private ChienDichMaGiamGiaRepository chienDichMaGiamGiaRepository;
    @Autowired
    public ChienDichMaGiamGiaServiceImpl(ChienDichMaGiamGiaRepository chienDichMaGiamGiaRepository) {
        this.chienDichMaGiamGiaRepository = chienDichMaGiamGiaRepository;
    }

    @Override
    public List<ChienDichGiamGia> findAll() {
        return chienDichMaGiamGiaRepository.findAll();
    }

    @Override
    public ChienDichGiamGia findById(int theId) {
//        Kiểu dữ liệu Optional giúp xử lý trường hợp dữ liệu có thể null một cách an toàn.
        Optional<ChienDichGiamGia> result = chienDichMaGiamGiaRepository.findById(theId);
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
        return chienDichMaGiamGiaRepository.save(chienDichGiamGia);
    }

    @Override
    public void deleteById(int theId) {
        chienDichMaGiamGiaRepository.deleteById(theId);
    }
}
