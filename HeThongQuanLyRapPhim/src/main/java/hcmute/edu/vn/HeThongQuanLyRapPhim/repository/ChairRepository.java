package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Ghe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ChairRepository extends JpaRepository<Ghe, Integer> {
    // Lấy PhongChieuPhim theo idPhongChieuPhim với JOIN FETCH để tải dsDayGhe và dsGhe
    @Query("SELECT DISTINCT p FROM PhongChieuPhim p " +
            "LEFT JOIN FETCH p.dsDayGhe d " +
            "LEFT JOIN FETCH d.dsGhe " +
            "WHERE p.idPhongChieuPhim = :idPhongChieuPhim")
    PhongChieuPhim findPhongChieuPhimById(@Param("idPhongChieuPhim") int idPhongChieuPhim);
}