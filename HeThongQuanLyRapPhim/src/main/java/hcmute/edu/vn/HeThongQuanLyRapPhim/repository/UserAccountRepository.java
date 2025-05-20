package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TKDoiTuongSuDung;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<TKDoiTuongSuDung, Integer> {
    TKDoiTuongSuDung findByTenDangNhap(String tenDangNhap);
}