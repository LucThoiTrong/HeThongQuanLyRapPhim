package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cinema")
public class CinemaController {

    @Autowired
    private CinemaService cinemaService;

    // Lấy danh sách tất cả rạp phim
    @GetMapping("/get-all")
    public ResponseEntity<List<RapPhim>> getAllCinemas() {
        List<RapPhim> dsRapPhim = cinemaService.getAllCinemas();
        return ResponseEntity.ok(dsRapPhim);
    }

    // Lấy thông tin một rạp phim theo ID
    @GetMapping("/{id}")
    public ResponseEntity<RapPhim> getCinemaByID(@PathVariable int id) {
        RapPhim rapPhim = cinemaService.getCinemaById(id);
        if (rapPhim != null) {
            return ResponseEntity.ok(rapPhim);
        }
        return ResponseEntity.notFound().build();
    }

    // Thêm rạp phim mới
    @PostMapping("/add")
    public ResponseEntity<RapPhim> addNewCinema(@RequestBody RapPhim rapPhim) {
        RapPhim rapPhimMoi = cinemaService.createCinema(rapPhim);
        return ResponseEntity.ok(rapPhimMoi);
    }

    // Sửa thông tin rạp phim
    @PutMapping("/update/{id}")
    public ResponseEntity<RapPhim> updateCinema(@PathVariable int id, @RequestBody RapPhim rapPhim) {
        RapPhim rapPhimMoi = cinemaService.updateCinema(id, rapPhim);
        if (rapPhimMoi != null) {
            return ResponseEntity.ok(rapPhimMoi);
        }
        return ResponseEntity.notFound().build();
    }

    // Xóa dãy ghế
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCinema(@PathVariable int id) {
        boolean isDeleted = cinemaService.deleteCinema(id);
        if (isDeleted) {
            return ResponseEntity.ok("Xóa rạp phim thành công");
        }
        return ResponseEntity.status(404).body("Không tìm thấy rạp phim để xóa");
    }
}
