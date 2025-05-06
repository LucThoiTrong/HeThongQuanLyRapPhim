package hcmute.edu.vn.HeThongQuanLyRapPhim.repository;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ComboBapNuoc;
import org.springframework.data.jpa.repository.JpaRepository;
//JpaRepository<ComboBapNuoc, Integer>
//
//ComboBapNuoc: Kiểu Entity tương ứng với bảng combo_bap_nuoc.
//
//Integer: Kiểu dữ liệu của khóa chính (id_combo_bap_nuoc) trong bảng
public interface PopcornDrinkComboRepository extends JpaRepository<ComboBapNuoc, Integer> {
}
