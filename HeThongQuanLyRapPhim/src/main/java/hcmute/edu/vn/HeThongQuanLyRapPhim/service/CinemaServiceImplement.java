package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CinemaServiceImplement implements CinemaService {

    @Autowired
    private CinemaRepository cinemaRepository;

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
        Optional<RapPhim> optionalRapPhim = cinemaRepository.findById(id);
        if (optionalRapPhim.isPresent()) {
            RapPhim rapPhim = optionalRapPhim.get();
            rapPhim.setTenRapPhim(rapPhimMoi.getTenRapPhim());
            rapPhim.setDiaChiRapPhim(rapPhimMoi.getDiaChiRapPhim());
            rapPhim.setTrangThaiRapPhim(rapPhimMoi.getTrangThaiRapPhim());
            rapPhim.setNhanVien(rapPhimMoi.getNhanVien());
            rapPhim.setDsPhongChieuPhim(rapPhimMoi.getDsPhongChieuPhim());
            return cinemaRepository.save(rapPhim);
        }
        return null;
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
}

