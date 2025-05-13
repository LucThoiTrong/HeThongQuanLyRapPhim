package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CinemaServiceImplement implements CinemaService {

    private final CinemaRepository cinemaRepository;

    @Autowired
    public CinemaServiceImplement(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public List<RapPhim> isCinemaWithoutManager() {
        return cinemaRepository.findAll().stream().filter(x -> x.getNhanVien() == null).toList();
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
    public RapPhim updateCinema(int id, RapPhim rapPhim) {
        return cinemaRepository.save(rapPhim); // Chỉ lưu trực tiếp, không xử lý logic
    }

    @Override
    public boolean deleteCinema(int id) {
        Optional<RapPhim> optionalRapPhim = cinemaRepository.findById(id);
        if (optionalRapPhim.isPresent()) {
            cinemaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public RapPhim findCinemaByName(String tenRapPhim) {
        return cinemaRepository.findByTenRapPhim(tenRapPhim);
    }
}