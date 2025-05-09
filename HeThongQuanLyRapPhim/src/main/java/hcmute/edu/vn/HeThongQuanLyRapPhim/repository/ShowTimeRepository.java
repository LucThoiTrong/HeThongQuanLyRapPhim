package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DayGhe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.SuatChieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowTimeRepository extends JpaRepository<SuatChieu, Integer> {

    // Tìm tất cả suất chiếu của phòng chiếu tương ứng
    @Query("SELECT sc FROM SuatChieu sc WHERE sc.phongChieuPhim.idPhongChieuPhim = :idPhongChieuPhim")
    List<SuatChieu> findByIdPhongChieuPhim(@Param("idPhongChieuPhim") int idPhongChieuPhim);
}