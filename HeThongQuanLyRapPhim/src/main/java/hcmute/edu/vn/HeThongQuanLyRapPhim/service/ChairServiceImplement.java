package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Ghe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.ChairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChairServiceImplement implements ChairService {

    @Autowired
    private ChairRepository chairRepository;

    @Override
    public List<Ghe> getAllChair() {
        return chairRepository.findAll();
    }

    @Override
    public Ghe getChairById(int id) {
        return chairRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ghế không tồn tại với ID: " + id));
    }

    @Override
    public Ghe createChair(Ghe ghe) {
        return chairRepository.save(ghe);
    }

    @Override
    public Ghe updateChair(int id, Ghe gheMoi) {
        Ghe gheCu = chairRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ghế không tồn tại với ID: " + id));
        gheCu.setTrangThaiGhe(gheMoi.isTrangThaiGhe());
        return chairRepository.save(gheCu);
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