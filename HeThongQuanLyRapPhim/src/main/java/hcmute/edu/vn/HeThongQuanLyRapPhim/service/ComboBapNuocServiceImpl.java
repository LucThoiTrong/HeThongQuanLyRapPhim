package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.ComboBapNuocRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ComboBapNuoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComboBapNuocServiceImpl implements ComboBapNuocService {
    private ComboBapNuocRepository comboBapNuocRepository;
    @Autowired
    public ComboBapNuocServiceImpl(ComboBapNuocRepository theComboBapNuocRepository) {
        this.comboBapNuocRepository = theComboBapNuocRepository;
    }

    @Override
    public List<ComboBapNuoc> findAll() {
        return comboBapNuocRepository.findAll();
    }

    @Override
    public ComboBapNuoc findById(int theId) {
//        Kiểu dữ liệu Optional giúp xử lý trường hợp dữ liệu có thể null một cách an toàn.
        Optional <ComboBapNuoc> result = comboBapNuocRepository.findById(theId);
        ComboBapNuoc comboBapNuoc = null;
        if (result.isPresent()) {
            comboBapNuoc = result.get();
        }
        else {
            // we didn't find the employee
            throw new RuntimeException("Did not find combo bap nuoc id - " + theId);
        }

        return comboBapNuoc;
    }

    @Override
    public ComboBapNuoc save(ComboBapNuoc comboBapNuoc) {
        return comboBapNuocRepository.save(comboBapNuoc);
    }

    @Override
    public void deleteById(int theId) {
        comboBapNuocRepository.deleteById(theId);
    }
}
