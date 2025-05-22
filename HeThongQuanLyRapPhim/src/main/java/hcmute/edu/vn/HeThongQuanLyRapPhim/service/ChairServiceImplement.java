package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Ghe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.ChairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChairServiceImplement implements ChairService {
    private final ChairRepository chairRepository;

    @Autowired
    public ChairServiceImplement(ChairRepository chairRepository) {
        this.chairRepository = chairRepository;
    }

    @Override
    public Ghe updateChair(int id, Ghe gheMoi) {
        Ghe gheCu = chairRepository.findById(id).orElse(null);
        if (gheCu != null) {
            gheCu.setTrangThaiGhe(gheMoi.isTrangThaiGhe());
            return chairRepository.save(gheCu);
        }
        return null;
    }

    @Override
    public PhongChieuPhim getPhongChieuPhimById(int idPhongChieuPhim) {
        PhongChieuPhim phongChieuPhim = chairRepository.findPhongChieuPhimById(idPhongChieuPhim);
        if (phongChieuPhim == null) {
            throw new RuntimeException("Phòng chiếu không tồn tại với ID: " + idPhongChieuPhim);
        }
        return phongChieuPhim;
    }
}