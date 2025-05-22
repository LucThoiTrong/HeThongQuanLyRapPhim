package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.LoaiDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository doiTuongSuDungRepository;

    @Autowired
    public UserServiceImpl(UserRepository doiTuongSuDungRepository) {
        this.doiTuongSuDungRepository = doiTuongSuDungRepository;
    }

    @Override
    public DoiTuongSuDung getDoiTuongSuDungById(int id) {
        return doiTuongSuDungRepository.findById(id).orElse(null);
    }

    // Phương thức riêng để lấy danh sách nhân viên chưa có nơi làm việc
    @Override
    public List<DoiTuongSuDung> getNhanVienChuaCoRap() {
        return doiTuongSuDungRepository.findByLoaiDoiTuongSuDungAndRapPhimIsNull(LoaiDoiTuongSuDung.NHAN_VIEN);
    }
}
