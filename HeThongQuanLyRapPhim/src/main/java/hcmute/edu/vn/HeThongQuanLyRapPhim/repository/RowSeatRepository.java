package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DayGhe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RowSeatRepository extends JpaRepository<DayGhe, Long> {
    List<DayGhe> findByPhongChieuPhim(PhongChieuPhim phongChieuPhim);
}
