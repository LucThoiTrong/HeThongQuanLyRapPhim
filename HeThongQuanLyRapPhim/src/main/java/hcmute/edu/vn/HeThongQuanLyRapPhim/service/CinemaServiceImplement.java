package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaServiceImplement implements CinemaService {

    private final CinemaRepository cinemaRepository;

    @Autowired
    public CinemaServiceImplement(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public List<RapPhim> getAllCinemas() {
        return cinemaRepository.findAll();
    }

    @Override
    public RapPhim getCinemaById(int id) {
        return cinemaRepository.findById(id).orElse(null);
    }

    @Override
    public RapPhim createCinema(RapPhim rapPhim) {
        return cinemaRepository.save(rapPhim);
    }

    @Override
    public RapPhim updateCinema(int id, RapPhim rapPhimMoi) {
        RapPhim rapPhim = cinemaRepository.findById(id).orElse(null);
        if (rapPhim != null) {
            return cinemaRepository.save(rapPhimMoi);
        }
        return null;
    }

    @Override
    public boolean deleteCinema(int id) {
        RapPhim rapPhim = cinemaRepository.findById(id).orElse(null);
        if (rapPhim != null) {
            cinemaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

