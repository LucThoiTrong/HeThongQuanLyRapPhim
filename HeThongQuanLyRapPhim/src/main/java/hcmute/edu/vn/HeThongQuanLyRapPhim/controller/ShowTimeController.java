package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.*;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.CinemaService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MovieService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.RoomService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.ShowTimeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public String showList(Model model) {
        List<SuatChieu> dsSuatChieu = showTimeService.getAllShowTimes();
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
            return "AddShowTime";
        }
        try {
            PhongChieuPhim phongChieu = roomService.getRoomById(suatChieu.getPhongChieuPhim().getIdPhongChieuPhim());
            Phim phim = movieService.getMovieById(suatChieu.getPhim().getIdPhim());
            if (phongChieu == null || phim == null) {
                redirectAttributes.addFlashAttribute("message", "Phòng chiếu hoặc phim không hợp lệ");
                return "redirect:/showtimes/";
            }
            suatChieu.setPhongChieuPhim(phongChieu);
            suatChieu.setPhim(phim);
            SuatChieu savedShowTime = showTimeService.createShowTime(suatChieu);
            redirectAttributes.addFlashAttribute("message", "Thêm suất chiếu thành công");
        } catch (Exception e) {
            logger.error("Error creating showtime: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("message", "Thêm suất chiếu thất bại: " + e.getMessage());
        }
        return "redirect:/showtimes/";
    }

    // Hiển thị form chỉnh sửa suất chiếu
    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        @SuppressWarnings("unchecked")
        List<SuatChieu> dsSuatChieu = (List<SuatChieu>) session.getAttribute("showtimes");
        SuatChieu suatChieu = showTimeService.getShowTimeById(id);
        if (suatChieu == null) {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy suất chiếu với ID: " + id);
            return "redirect:/showtimes/";
        } else {
            suatChieu = dsSuatChieu.stream()
                    .filter(s -> s.getIdSuatChieu() == id)
                    .findFirst().orElse(null);
        }
        if (suatChieu == null) {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy suất chiếu với ID: " + id);
            return "redirect:/showtimes/";
        }

        // Kiểm tra và ghi log ngayGioChieu
        System.out.println("SuatChieu ID: " + id + ", ngayGioChieu: " + suatChieu.getNgayGioChieu());
        if (suatChieu.getNgayGioChieu() == null) {
            suatChieu.setNgayGioChieu(LocalDateTime.now());
            System.out.println("ngayGioChieu was null, set to: " + suatChieu.getNgayGioChieu());
        }

        // Khởi tạo phongChieuPhim và phim nếu null
        if (suatChieu.getPhongChieuPhim() == null) {
            suatChieu.setPhongChieuPhim(new PhongChieuPhim());
        }
        if (suatChieu.getPhim() == null) {
            suatChieu.setPhim(new Phim());
        }

        // Lấy danh sách phòng chiếu dựa trên rạp phim
        List<PhongChieuPhim> phongChieuList = Collections.emptyList();
        Integer selectedRapPhimId = null;
        if (suatChieu.getPhongChieuPhim().getRapPhim() != null) {
            selectedRapPhimId = suatChieu.getPhongChieuPhim().getRapPhim().getIdRapPhim();
            phongChieuList = roomService.getAllRoomsByCinemaId(selectedRapPhimId);
        }

        model.addAttribute("suatChieu", suatChieu);
        model.addAttribute("hinhThucChieuList", Arrays.asList(HinhThucChieu.values()));
        model.addAttribute("phimList", movieService.getAllMovies());
        model.addAttribute("rapPhimList", cinemaService.getAllCinemas());
        model.addAttribute("phongChieuList", phongChieuList);
        model.addAttribute("selectedRapPhimId", selectedRapPhimId);
        return "EditShowTime";
    }

    // Xử lý cập nhật suất chiếu
    @PostMapping("/update/{id}")
    public String updateShowTime(@Valid @ModelAttribute("suatChieu") SuatChieu suatChieu, BindingResult result, @PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("hinhThucChieuList", Arrays.asList(HinhThucChieu.values()));
            model.addAttribute("phimList", movieService.getAllMovies());
            model.addAttribute("rapPhimList", cinemaService.getAllCinemas());
            model.addAttribute("phongChieuList", Collections.emptyList());
            model.addAttribute("selectedRapPhimId", suatChieu.getPhongChieuPhim() != null && suatChieu.getPhongChieuPhim().getRapPhim() != null ? suatChieu.getPhongChieuPhim().getRapPhim().getIdRapPhim() : null);
            return "EditShowTime";
        }
        SuatChieu updatedShowTime = showTimeService.updateShowTime(id, suatChieu);
        if (updatedShowTime != null) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật suất chiếu thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Cập nhật suất chiếu thất bại");
        }
        return "redirect:/showtimes/";
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

    private static final Logger logger = LoggerFactory.getLogger(ShowTimeController.class);

    @GetMapping("/api/rooms/{idRapPhim}")
    public ResponseEntity<List<PhongChieuPhim>> getRoomsByCinemaId(@PathVariable int idRapPhim) {
        try {
            List<PhongChieuPhim> rooms = roomService.getAllRoomsByCinemaId(idRapPhim);
            return ResponseEntity.ok(rooms != null ? rooms : Collections.emptyList());
        } catch (Exception e) {
            logger.error("Error fetching rooms for cinema ID {}: {}", idRapPhim, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}