package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DayGhe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RowChairRepository extends JpaRepository<DayGhe, Integer> {
    // JpaRepository đã cung cấp sẵn các phương thức như save(), findById(), deleteById(), findAll()

    // Tìm tất cả dãy ghế của phòng chiếu tương ứng
    @Query("SELECT d FROM DayGhe d WHERE d.phongChieuPhim.idPhongChieuPhim = :idPhongChieuPhim")
    List<DayGhe> findAllRowChairByIdRoom(@Param("idPhongChieuPhim") int idPhongChieuPhim);
}
