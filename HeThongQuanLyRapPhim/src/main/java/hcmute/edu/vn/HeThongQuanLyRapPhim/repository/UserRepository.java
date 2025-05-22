package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.LoaiDoiTuongSuDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<DoiTuongSuDung, Integer> {
    // Tìm nhân viên chưa có rạp làm việc
    @Query("SELECT d FROM DoiTuongSuDung d " +
            "WHERE d.loaiDoiTuongSuDung = :loaiDoiTuongSuDung AND d.idDoiTuongSuDung NOT IN " +
            "(SELECT r.nhanVien.idDoiTuongSuDung FROM RapPhim r WHERE r.nhanVien IS NOT NULL)")
    List<DoiTuongSuDung> findByLoaiDoiTuongSuDungAndRapPhimIsNull(LoaiDoiTuongSuDung loaiDoiTuongSuDung);

    DoiTuongSuDung findDoiTuongSuDungByEmail(String email);

    List<DoiTuongSuDung> findAllByLoaiDoiTuongSuDung(LoaiDoiTuongSuDung loaiDoiTuongSuDung);
}
