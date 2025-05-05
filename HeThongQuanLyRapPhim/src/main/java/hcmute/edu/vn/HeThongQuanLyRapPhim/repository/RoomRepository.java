package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<PhongChieuPhim, Integer> {
    // JpaRepository đã cung cấp sẵn các phương thức như save(), findById(), deleteById(), findAll()

    // Tìm tất cả phòng chiếu cho các rạp tương ứng
    @Query("SELECT p FROM PhongChieuPhim p WHERE p.rapPhim.idRapPhim = :idRapPhim")
    List<PhongChieuPhim> findAllRoomByIdCinema(@Param("idRapPhim") int idRapPhim);
}
