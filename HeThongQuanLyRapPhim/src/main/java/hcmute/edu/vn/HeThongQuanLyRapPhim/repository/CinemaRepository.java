package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinemaRepository extends JpaRepository<RapPhim, Integer> {
    // JpaRepository đã cung cấp sẵn các phương thức như save(), findById(), deleteById(), findAll()
    RapPhim findByTenRapPhim(String tenRapPhim);
}
