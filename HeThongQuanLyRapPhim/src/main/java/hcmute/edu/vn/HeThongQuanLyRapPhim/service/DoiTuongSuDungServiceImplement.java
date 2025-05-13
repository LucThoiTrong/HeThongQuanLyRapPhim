package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.LoaiDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.DoiTuongSuDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoiTuongSuDungServiceImplement implements DoiTuongSuDungService {
    private final DoiTuongSuDungRepository doiTuongSuDungRepository;

    @Autowired
    public DoiTuongSuDungServiceImplement(DoiTuongSuDungRepository doiTuongSuDungRepository) {
        this.doiTuongSuDungRepository = doiTuongSuDungRepository;
    }

    @Override
    public List<DoiTuongSuDung> getAllDoiTuongSuDung() {
        return doiTuongSuDungRepository.findAll();
    }

    @Override
    public DoiTuongSuDung getDoiTuongSuDungById(int id) {
        return doiTuongSuDungRepository.findById(id).orElse(null);
    }

    @Override
    public DoiTuongSuDung createDoiTuongSuDung(DoiTuongSuDung doiTuongSuDung) {
        return doiTuongSuDungRepository.save(doiTuongSuDung);
    }

    @Override
    public DoiTuongSuDung updateDoiTuongSuDung(DoiTuongSuDung doiTuongSuDungMoi) {
        return doiTuongSuDungRepository.save(doiTuongSuDungMoi);
    }

    @Override
    public boolean deleteDoiTuongSuDung(int id) {
        Optional<DoiTuongSuDung> optionalDoiTuongSuDung = doiTuongSuDungRepository.findById(id);
        if (optionalDoiTuongSuDung.isPresent()) {
            doiTuongSuDungRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Phương thức riêng để lấy danh sách nhân viên chưa có nơi làm việc
    @Override
    public List<DoiTuongSuDung> getNhanVienChuaCoRap() {
        return doiTuongSuDungRepository.findByLoaiDoiTuongSuDungAndRapPhimIsNull(LoaiDoiTuongSuDung.NHAN_VIEN);
    }

}
