package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<HoaDon, Integer> {
    List<HoaDon> findByDoiTuongSuDungIdDoiTuongSuDung(int idDoiTuongSuDung);

}
