package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.LoaiDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.CinemaRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CinemaServiceImplement implements CinemaService {

    private final CinemaRepository cinemaRepository;
    private final UserRepository userRepository;

    @Autowired
    public CinemaServiceImplement(CinemaRepository cinemaRepository, UserRepository userRepository) {
        this.cinemaRepository = cinemaRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<DoiTuongSuDung> getNhanVienChuaCoRap() {
        return userRepository.findByLoaiDoiTuongSuDungAndRapPhimIsNull(LoaiDoiTuongSuDung.NHAN_VIEN);
    }

    @Override
    public List<DoiTuongSuDung> getDanhSachNhanVien(RapPhim rapPhim) {
        List<DoiTuongSuDung> result = getNhanVienChuaCoRap();
        DoiTuongSuDung currentNhanVien = rapPhim.getNhanVien();

        if (currentNhanVien != null) {
            if (result == null) {
                result = new ArrayList<>();
            }
            result.add(currentNhanVien);
        }
        return result;

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
        RapPhim rapPhimCu = cinemaRepository.findById(id).orElse(null);
        if (rapPhimCu != null) {
            rapPhimCu.setTenRapPhim(rapPhimMoi.getTenRapPhim());
            rapPhimCu.setDiaChiRapPhim(rapPhimMoi.getDiaChiRapPhim());
            rapPhimCu.setTrangThaiRapPhim(rapPhimMoi.getTrangThaiRapPhim());
            rapPhimCu.setNhanVien(rapPhimMoi.getNhanVien());
            return cinemaRepository.save(rapPhimCu);
        }
        return null;
    }

    @Override
    public boolean deleteCinema(int id) {
        RapPhim rapPhim = cinemaRepository.findById(id).orElse(null);
        if (rapPhim != null) {
            if(rapPhim.getNhanVien() != null) {
                rapPhim.getNhanVien().setRapPhim(null);
                cinemaRepository.save(rapPhim);
            }
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