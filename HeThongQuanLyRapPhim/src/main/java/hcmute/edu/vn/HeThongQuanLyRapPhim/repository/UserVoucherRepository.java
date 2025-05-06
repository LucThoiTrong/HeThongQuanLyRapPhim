package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserVoucherRepository extends JpaRepository<MaGiamGia, Integer> {
    Set<MaGiamGia> findAllByDoiTuongSuDung(DoiTuongSuDung doiTuongSuDung);
}