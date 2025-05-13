package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HinhThucChieu;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.SuatChieu;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.CinemaService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MovieService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.RoomService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.ShowTimeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/showtimes")
public class ShowTimeController {

    private final ShowTimeService showTimeService;
    private final MovieService movieService;
    private final RoomService roomService;
    private final CinemaService cinemaService;

    @Autowired
    public ShowTimeController(ShowTimeService showTimeService, MovieService movieService, RoomService roomService, CinemaService cinemaService) {
        this.showTimeService = showTimeService;
        this.movieService = movieService;
        this.roomService = roomService;
        this.cinemaService = cinemaService;
    }

    // Hiển thị danh sách suất chiếu
    @GetMapping("/")
    public String showList(Model model, HttpSession session) {
        List<SuatChieu> dsSuatChieu = showTimeService.getAllShowTimes();
//        session.setAttribute("showtimes", dsSuatChieu);
        model.addAttribute("showtimes", dsSuatChieu);
        return "ShowTimeListPage";
    }

    // Hiển thị form thêm suất chiếu
    @GetMapping("/new")
    public String showAddForm(Model model) {
        SuatChieu suatChieu = new SuatChieu();
        suatChieu.setPhongChieuPhim(new PhongChieuPhim());
        suatChieu.setPhim(new Phim());
        suatChieu.setNgayGioChieu(LocalDateTime.now());
        model.addAttribute("suatChieu", suatChieu);
        model.addAttribute("hinhThucChieuList", Arrays.asList(HinhThucChieu.values()));
        model.addAttribute("phimList", movieService.getAllMovies());
        model.addAttribute("rapPhimList", cinemaService.getAllCinemas());
        model.addAttribute("phongChieuList", Collections.emptyList());
        return "AddShowTime";
    }

    // Xử lý thêm suất chiếu
    @PostMapping("/new")
    public String insertShowTime(@Valid @ModelAttribute("suatChieu") SuatChieu suatChieu, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("hinhThucChieuList", Arrays.asList(HinhThucChieu.values()));
            model.addAttribute("phimList", movieService.getAllMovies());
            model.addAttribute("rapPhimList", cinemaService.getAllCinemas());
            model.addAttribute("phongChieuList", Collections.emptyList());
            return "AddShowTime";
        }
        try {
            SuatChieu savedShowTime = showTimeService.createShowTime(suatChieu);
            if (savedShowTime != null) {
                redirectAttributes.addFlashAttribute("message", "Thêm suất chiếu thành công");
            } else {
                redirectAttributes.addFlashAttribute("message", "Thêm suất chiếu thất bại");
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("hinhThucChieuList", Arrays.asList(HinhThucChieu.values()));
            model.addAttribute("phimList", movieService.getAllMovies());
            model.addAttribute("rapPhimList", cinemaService.getAllCinemas());
            model.addAttribute("phongChieuList", Collections.emptyList());
            model.addAttribute("message", e.getMessage());
            return "AddShowTime";
        }
        return "redirect:/showtimes/";
    }

    // Hiển thị form chỉnh sửa suất chiếu
    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        SuatChieu suatChieu = showTimeService.getShowTimeById(id);
        if (suatChieu == null || suatChieu.getPhongChieuPhim() == null || suatChieu.getPhongChieuPhim().getRapPhim() == null) {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy suất chiếu hoặc thông tin phòng chiếu/rạp phim không hợp lệ.");
            return "redirect:/showtimes/";
        }

        List<HinhThucChieu> hinhThucChieuList = Arrays.asList(HinhThucChieu.values());
        int idRapPhim = suatChieu.getPhongChieuPhim().getRapPhim().getIdRapPhim();
        List<PhongChieuPhim> phongChieuList = roomService.getAllRoomsByCinemaId(idRapPhim);

        model.addAttribute("suatChieu", suatChieu);
        model.addAttribute("hinhThucChieuList", hinhThucChieuList);
        model.addAttribute("phimList", movieService.getAllMovies());
        model.addAttribute("rapPhimList", cinemaService.getAllCinemas());
        model.addAttribute("phongChieuList", phongChieuList);
        model.addAttribute("selectedRapPhimId", idRapPhim);
        return "EditShowTime";
    }

    // Xử lý cập nhật suất chiếu
    @PostMapping("/update/{id}")
    public String updateShowTime(@Valid @ModelAttribute("suatChieu") SuatChieu suatChieu, BindingResult result, @PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        // Log dữ liệu nhận được từ form để debug
        System.out.println("Received SuatChieu: " + suatChieu);
        System.out.println("Phim ID: " + (suatChieu.getPhim() != null ? suatChieu.getPhim().getIdPhim() : null));
        System.out.println("PhongChieuPhim ID: " + (suatChieu.getPhongChieuPhim() != null ? suatChieu.getPhongChieuPhim().getIdPhongChieuPhim() : null));
        System.out.println("NgayGioChieu: " + suatChieu.getNgayGioChieu());
        System.out.println("HinhThucChieu: " + suatChieu.getHinhThucChieu());

        // Khởi tạo model attributes cho trường hợp lỗi
        Integer selectedRapPhimId = suatChieu.getPhongChieuPhim() != null && suatChieu.getPhongChieuPhim().getRapPhim() != null ? suatChieu.getPhongChieuPhim().getRapPhim().getIdRapPhim() : null;
        List<PhongChieuPhim> phongChieuList = selectedRapPhimId != null ? roomService.getAllRoomsByCinemaId(selectedRapPhimId) : Collections.emptyList();
        model.addAttribute("hinhThucChieuList", Arrays.asList(HinhThucChieu.values()));
        model.addAttribute("phimList", movieService.getAllMovies());
        model.addAttribute("rapPhimList", cinemaService.getAllCinemas());
        model.addAttribute("phongChieuList", phongChieuList);
        model.addAttribute("selectedRapPhimId", selectedRapPhimId);

        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            return "EditShowTime";
        }

        try {
            // Kiểm tra và ánh xạ phim từ phim.idPhim
            if (suatChieu.getPhim() == null || suatChieu.getPhim().getIdPhim() == null) {
                model.addAttribute("message", "Vui lòng chọn phim.");
                return "EditShowTime";
            }
            Phim phim = movieService.getMovieById(suatChieu.getPhim().getIdPhim());
            if (phim == null || phim.getThoiLuongChieu() == null) {
                model.addAttribute("message", "Phim không hợp lệ hoặc thiếu thời lượng chiếu.");
                return "EditShowTime";
            }
            suatChieu.setPhim(phim);

            // Kiểm tra phòng chiếu
            if (suatChieu.getPhongChieuPhim() == null || suatChieu.getPhongChieuPhim().getIdPhongChieuPhim() == null) {
                model.addAttribute("message", "Vui lòng chọn phòng chiếu.");
                return "EditShowTime";
            }

            SuatChieu updatedShowTime = showTimeService.updateShowTime(id, suatChieu);
            if (updatedShowTime != null) {
                redirectAttributes.addFlashAttribute("message", "Cập nhật suất chiếu thành công");
                return "redirect:/showtimes/";
            } else {
                model.addAttribute("message", "Không tìm thấy suất chiếu để cập nhật.");
                return "EditShowTime";
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            return "EditShowTime";
        }
    }

    // Xóa suất chiếu
    @PostMapping("/delete/{id}")
    public String deleteShowTime(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        boolean isSuccess = showTimeService.deleteShowTimeById(id);
        if (isSuccess) {
            redirectAttributes.addFlashAttribute("message", "Xóa suất chiếu thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Xóa suất chiếu thất bại");
        }
        return "redirect:/showtimes/";
    }

    @GetMapping("/api/rooms/{idRapPhim}")
    public ResponseEntity<List<PhongChieuPhim>> getRoomsByCinemaId(@PathVariable int idRapPhim) {
        try {
            List<PhongChieuPhim> rooms = roomService.getAllRoomsByCinemaId(idRapPhim);
            if (rooms == null || rooms.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    // API để lấy danh sách phòng chiếu theo idRapPhim
    @GetMapping("/rooms/{idRapPhim}")
    @ResponseBody
    public List<PhongChieuPhim> getRoomsByRapPhimId(@PathVariable int idRapPhim) {
        return roomService.getAllRoomsByCinemaId(idRapPhim);
    }
}