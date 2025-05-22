package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ComboBapNuoc;

import java.util.List;

public interface PopcornDrinkComboService {
    List<ComboBapNuoc> findAll();

    ComboBapNuoc findById(int theId);
    ComboBapNuoc insert(ComboBapNuoc comboBapNuoc);

    ComboBapNuoc update(int id, ComboBapNuoc comboBapNuoc);

    void deleteById(int theId);

}
