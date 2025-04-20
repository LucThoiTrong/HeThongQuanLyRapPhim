package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ComboBapNuoc;

import java.util.List;

public interface ComboBapNuocService {
    List<ComboBapNuoc> findAll();

    ComboBapNuoc findById(int theId);

    ComboBapNuoc save(ComboBapNuoc theEmployee);

    void deleteById(int theId);

}
