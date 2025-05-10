package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HinhThucChieu;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.SuatChieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<SuatChieu, Integer> {

    @Query("SELECT s FROM SuatChieu s WHERE s.phim = :phim AND DATE(s.ngayGioChieu) = :ngayChieu AND s.hinhThucChieu = :hinhThucChieu")
    List<SuatChieu> findByPhimAndNgayChieuAndHinhThucChieu(Phim phim, LocalDate ngayChieu, HinhThucChieu hinhThucChieu);
}
