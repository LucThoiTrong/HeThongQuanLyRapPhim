package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.LoaiDoiTuongSuDung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<DoiTuongSuDung, Integer> {
    List<DoiTuongSuDung> findAllByLoaiDoiTuongSuDung(LoaiDoiTuongSuDung loaiDoiTuongSuDung);
}
