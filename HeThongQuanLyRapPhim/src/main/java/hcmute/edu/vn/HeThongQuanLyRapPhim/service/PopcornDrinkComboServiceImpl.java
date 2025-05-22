package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.PopcornDrinkComboRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ComboBapNuoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PopcornDrinkComboServiceImpl implements PopcornDrinkComboService {
    private final PopcornDrinkComboRepository popCornDrinkComboRepository;

    @Autowired
    public PopcornDrinkComboServiceImpl(PopcornDrinkComboRepository popCornDrinkComboRepository) {
        this.popCornDrinkComboRepository = popCornDrinkComboRepository;
    }

    @Override
    public List<ComboBapNuoc> findAll() {
        return popCornDrinkComboRepository.findAll();
    }

    @Override
    public ComboBapNuoc findById(int theId) {
        return popCornDrinkComboRepository.findById(theId).orElse(null);
    }

    @Override
    public ComboBapNuoc insert(ComboBapNuoc comboBapNuoc) {
        return popCornDrinkComboRepository.save(comboBapNuoc);
    }

    @Override
    public ComboBapNuoc update(int id, ComboBapNuoc comboBapNuoc) {
        ComboBapNuoc findComboBapNuoc = findById(id);
        if (findComboBapNuoc != null) {
            return popCornDrinkComboRepository.save(comboBapNuoc);
        }
        return null;
    }

    @Override
    public void deleteById(int theId) {
        popCornDrinkComboRepository.deleteById(theId);
    }
}
