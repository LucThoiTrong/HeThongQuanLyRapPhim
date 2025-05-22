package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountRepository extends JpaRepository<MaGiamGia, Integer> {
    List<MaGiamGia> findAllByDoiTuongSuDungIsNull();
}
