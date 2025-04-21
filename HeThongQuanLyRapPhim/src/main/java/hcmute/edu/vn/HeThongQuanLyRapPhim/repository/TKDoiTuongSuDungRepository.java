package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TKDoiTuongSuDung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TKDoiTuongSuDungRepository extends JpaRepository<TKDoiTuongSuDung, Integer> {
    boolean existsByTenDangNhap(String tenDangNhap);
    Optional<TKDoiTuongSuDung> findByTenDangNhap(String tenDangNhap);

}