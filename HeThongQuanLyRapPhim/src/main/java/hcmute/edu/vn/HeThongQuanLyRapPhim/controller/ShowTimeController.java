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
import java.time.format.DateTimeFormatter;
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
        session.setAttribute("showtimes", dsSuatChieu);
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
        SuatChieu savedShowTime = showTimeService.createShowTime(suatChieu);
        if (savedShowTime != null) {
            redirectAttributes.addFlashAttribute("message", "Thêm suất chiếu thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Thêm suất chiếu thất bại");
        }
        return "redirect:/showtimes/";
    }

    // Hiển thị form chỉnh sửa suất chiếu
    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        // Lấy suất chiếu
        SuatChieu suatChieu = showTimeService.getShowTimeById(id);

        // Lấy danh sách hình thức chiếu
        List<HinhThucChieu> hinhThucChieuList = Arrays.asList(HinhThucChieu.values());

        // Lấy danh sách phòng chiếu dựa trên id rạp phim
        int idRapPhim = suatChieu.getPhongChieuPhim().getRapPhim().getIdRapPhim();
        List<PhongChieuPhim> phongChieuList = roomService.getAllRoomsByCinemaId(idRapPhim);
        LocalDateTime ngayGioChieu = suatChieu.getNgayGioChieu();
        System.out.println(ngayGioChieu);


        model.addAttribute("suatChieu", suatChieu);
        model.addAttribute("hinhThucChieuList", hinhThucChieuList);
        model.addAttribute("phimList", movieService.getAllMovies());
        model.addAttribute("rapPhimList", cinemaService.getAllCinemas());
        model.addAttribute("phongChieuList", phongChieuList);
        model.addAttribute("ngayGioChieu", ngayGioChieu);
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

    // API để lấy danh sách phòng chiếu theo idRapPhim
    @GetMapping("/rooms/{idRapPhim}")
    @ResponseBody
    public List<PhongChieuPhim> getRoomsByRapPhimId(@PathVariable int idRapPhim) {
        return roomService.getAllRoomsByCinemaId(idRapPhim);
    }
}