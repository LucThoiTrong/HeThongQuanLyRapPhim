package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiPhim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Phim, Integer>, JpaSpecificationExecutor<Phim> {
    Optional<Phim> findByTenPhim(String tenPhim);

    List<Phim> findByTrangThaiPhim(TrangThaiPhim trangThaiPhim);

    List<Phim> getAllByTrangThaiPhimIsNot(TrangThaiPhim trangThaiPhim);
}
