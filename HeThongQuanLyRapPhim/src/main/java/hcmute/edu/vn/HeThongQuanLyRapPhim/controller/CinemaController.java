package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiRapPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/cinemas")
public class CinemaController {
    private final CinemaService cinemaService;

    @Autowired
    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping("/")
    public String showList(Model model) {
        List<RapPhim> dsRapPhim = cinemaService.getAllCinemas();

        model.addAttribute("cinemas", dsRapPhim);

        List<DoiTuongSuDung> nhanVienList = cinemaService.getNhanVienChuaCoRap();
        model.addAttribute("nhanVienList", nhanVienList != null ? nhanVienList : new ArrayList<>());

        return "CinemaPage";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("rapPhim", new RapPhim());
        model.addAttribute("trangThaiList", Arrays.asList(TrangThaiRapPhim.values()));
        List<DoiTuongSuDung> nhanVienList = cinemaService.getNhanVienChuaCoRap();
        model.addAttribute("nhanVienList", nhanVienList != null ? nhanVienList : new ArrayList<>());
        return "AddCinemaPage";
    }

    @PostMapping("/new")
    public String insertRapPhim(@ModelAttribute("rapPhim") RapPhim rapPhim, RedirectAttributes redirectAttributes) {
        RapPhim existingCinema = cinemaService.findCinemaByName(rapPhim.getTenRapPhim());
        if (existingCinema != null) {
            redirectAttributes.addFlashAttribute("tenRapPhimError", "Tên rạp phim đã tồn tại, vui lòng chọn tên khác!");
            redirectAttributes.addFlashAttribute("rapPhim", rapPhim); // Giữ lại dữ liệu đã nhập
            return "redirect:/cinemas/new";
        }
        RapPhim rp = cinemaService.createCinema(rapPhim);
        if (rp != null) {
            redirectAttributes.addFlashAttribute("message", "Thêm rạp phim thành công");
        } else {
            redirectAttributes.addFlashAttribute("error", "Thêm rạp phim thất bại");
        }
        return "redirect:/cinemas/";
    }

    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        RapPhim rapPhim = cinemaService.getCinemaById(id);

        List<DoiTuongSuDung> nhanVienList = cinemaService.getDanhSachNhanVien(rapPhim);

        model.addAttribute("rapPhim", rapPhim);
        model.addAttribute("trangThaiList", Arrays.asList(TrangThaiRapPhim.values()));
        model.addAttribute("nhanVienList", nhanVienList != null ? nhanVienList : new ArrayList<>());
        return "EditCinemaPage";
    }

    @PostMapping("/update/{id}")
    public String updateRapPhim(@ModelAttribute("rapPhim") RapPhim rapPhimMoi, @PathVariable int id, RedirectAttributes redirectAttributes) {
        RapPhim existingCinema = cinemaService.findCinemaByName(rapPhimMoi.getTenRapPhim());
        if (existingCinema != null && existingCinema.getIdRapPhim() != id) {
            redirectAttributes.addFlashAttribute("tenRapPhimError", "Tên rạp phim đã tồn tại, vui lòng chọn tên khác!");
            redirectAttributes.addFlashAttribute("rapPhim", rapPhimMoi);
            return "redirect:/cinemas/update/" + id;
        }

        // Cập nhật các trường trong controller
        RapPhim rp = cinemaService.updateCinema(id, rapPhimMoi);
        if (rp != null) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật rạp phim thành công");
        } else {
            redirectAttributes.addFlashAttribute("error", "Cập nhật rạp phim thất bại");
        }

        return "redirect:/cinemas/";
    }

    @PostMapping("/delete/{id}")
    public String deleteCinema(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        boolean isSuccess = cinemaService.deleteCinema(id);
        if (isSuccess) {
            redirectAttributes.addFlashAttribute("message", "Xóa rạp phim thành công");
        } else {
            redirectAttributes.addFlashAttribute("error", "Xóa rạp phim thất bại");
        }
        return "redirect:/cinemas/";
    }
}