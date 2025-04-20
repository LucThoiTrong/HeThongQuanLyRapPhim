package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;

import java.util.List;

public interface CinemaService {
    List<RapPhim> getAllCinemas();
    RapPhim getCinemaById(int id);
    RapPhim createCinema(RapPhim rapPhim);
    RapPhim updateCinema(int id, RapPhim rapPhim);
    boolean deleteCinema(int id);
}
