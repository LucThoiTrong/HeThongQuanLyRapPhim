package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiPhim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Phim, Integer> {
    // Tìm theo tên phim
    Optional<Phim> findByTenPhim(String tenPhim);

    List<Phim> findByTrangThaiPhim(TrangThaiPhim trangThaiPhim);

    List<Phim> getAllByTrangThaiPhimIsNot(TrangThaiPhim trangThaiPhim);
}
