package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<PhongChieuPhim, Integer> {
    List<PhongChieuPhim> findAllByRapPhim(RapPhim rapPhim);
    PhongChieuPhim findByTenPhongChieuPhim(String name);
}