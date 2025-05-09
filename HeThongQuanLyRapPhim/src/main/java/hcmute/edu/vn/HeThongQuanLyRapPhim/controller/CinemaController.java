package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiRapPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.CinemaService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.DoiTuongSuDungService;
import jakarta.servlet.http.HttpSession;
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
    private final DoiTuongSuDungService doiTuongSuDungService;

    @Autowired
    public CinemaController(CinemaService cinemaService, DoiTuongSuDungService doiTuongSuDungService) {
        this.cinemaService = cinemaService;
        this.doiTuongSuDungService = doiTuongSuDungService;
    }

    @GetMapping("/")
    public String showList(Model model, HttpSession session) {
        List<RapPhim> dsRapPhim = cinemaService.getAllCinemas();
        session.setAttribute("cinemas", dsRapPhim);
        model.addAttribute("cinemas", dsRapPhim);

        List<DoiTuongSuDung> nhanVienList = doiTuongSuDungService.getNhanVienChuaCoRap();
        model.addAttribute("nhanVienList", nhanVienList != null ? nhanVienList : new ArrayList<>());

        return "CinemaListPage";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("rapPhim", new RapPhim());
        model.addAttribute("trangThaiList", Arrays.asList(TrangThaiRapPhim.values()));
        List<DoiTuongSuDung> nhanVienList = doiTuongSuDungService.getNhanVienChuaCoRap();
        model.addAttribute("nhanVienList", nhanVienList != null ? nhanVienList : new ArrayList<>());
        return "AddCinema";
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
    public String showEditForm(@PathVariable("id") int id, Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<RapPhim> dsRapPhim = (List<RapPhim>) session.getAttribute("cinemas");
        RapPhim rapPhim;
        if (dsRapPhim == null) {
            rapPhim = cinemaService.getCinemaById(id);
        } else {
            rapPhim = dsRapPhim.stream()
                    .filter(p -> p.getIdRapPhim() == id)
                    .findFirst().orElse(null);
        }

        assert rapPhim != null;
        List<DoiTuongSuDung> nhanVienList = getNhanVienListChoRap(rapPhim);

        model.addAttribute("rapPhim", rapPhim);
        model.addAttribute("trangThaiList", Arrays.asList(TrangThaiRapPhim.values()));
        model.addAttribute("nhanVienList", nhanVienList != null ? nhanVienList : new ArrayList<>());
        return "EditCinema";
    }

    @PostMapping("/update/{id}")
    public String updateRapPhim(@ModelAttribute("rapPhim") RapPhim rapPhim, @PathVariable int id, RedirectAttributes redirectAttributes) {
        RapPhim existingCinema = cinemaService.findCinemaByName(rapPhim.getTenRapPhim());
        if (existingCinema != null && existingCinema.getIdRapPhim() != id) {
            redirectAttributes.addFlashAttribute("tenRapPhimError", "Tên rạp phim đã tồn tại, vui lòng chọn tên khác!");
            redirectAttributes.addFlashAttribute("rapPhim", rapPhim); // Giữ lại dữ liệu đã nhập
            return "redirect:/cinemas/update/" + id;
        }
        RapPhim rp = cinemaService.updateCinema(id, rapPhim);
        if (rp != null) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật rạp phim thành công");
        } else {
            redirectAttributes.addFlashAttribute("error", "Cập nhật rạp phim thất bại");
        }
        return "redirect:/cinemas/";
    }

    private List<DoiTuongSuDung> getNhanVienListChoRap(RapPhim rapPhim) {
        List<DoiTuongSuDung> nhanVienList = doiTuongSuDungService.getNhanVienChuaCoRap();
        DoiTuongSuDung currentNhanVien = rapPhim.getNhanVien();

        if (currentNhanVien != null) {
            if (nhanVienList == null) {
                nhanVienList = new ArrayList<>();
            }
            nhanVienList.add(currentNhanVien);
        }
        return nhanVienList;
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