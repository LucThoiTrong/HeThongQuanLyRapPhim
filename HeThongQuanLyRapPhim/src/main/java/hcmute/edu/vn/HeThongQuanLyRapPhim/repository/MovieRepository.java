package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiPhim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface MovieRepository extends JpaRepository<Phim, Integer> {
    Set<Phim> findByTrangThaiPhim(TrangThaiPhim trangThaiPhim);
}
