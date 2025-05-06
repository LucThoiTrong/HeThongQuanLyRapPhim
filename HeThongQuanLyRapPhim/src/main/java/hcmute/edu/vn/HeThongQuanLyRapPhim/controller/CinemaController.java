package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiRapPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.CinemaService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.DoiTuongSuDungService;
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

    // Lấy danh sách tất cả rạp phim
    @GetMapping("/")
    public String showList(Model model) {
        // Lấy danh sách rạp phim
        List<RapPhim> dsRapPhim = cinemaService.getAllCinemas();
        model.addAttribute("cinemas", dsRapPhim);

        List<DoiTuongSuDung> nhanVienList = doiTuongSuDungService.getNhanVienChuaCoRap();
        model.addAttribute("nhanVienList", nhanVienList != null ? nhanVienList : new ArrayList<>());

        return "CinemaListPage";
    }

    // Hiển thị form thêm rạp
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("rapPhim", new RapPhim());
        model.addAttribute("trangThaiList", Arrays.asList(TrangThaiRapPhim.values()));
        List<DoiTuongSuDung> nhanVienList = doiTuongSuDungService.getNhanVienChuaCoRap();
        model.addAttribute("nhanVienList", nhanVienList != null ? nhanVienList : new ArrayList<>());
        return "AddCinema";
    }

    // Xử lý thêm rạp
    @PostMapping("/new")
    public String insertCinema(@ModelAttribute("rapPhim") RapPhim rapPhim, RedirectAttributes redirectAttributes) {
        RapPhim rp = cinemaService.createCinema(rapPhim);
        if(rp != null) {
            redirectAttributes.addFlashAttribute("message", "Thêm rạp phim thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Thêm rạp phim thất bại");
        }
        return "redirect:/cinemas/";
    }

    // Hiển thị form điền khi cập nhật rạp
    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        RapPhim rapPhim = cinemaService.getCinemaById(id);

        // Lấy danh sách nhân viên chưa được phân công
        assert rapPhim != null;
        List<DoiTuongSuDung> nhanVienList = getNhanVienListChoRap(rapPhim);

        // Thêm các thuộc tính vào model
        model.addAttribute("rapPhim", rapPhim);
        // Trạng thái của 1 rạp phim
        model.addAttribute("trangThaiList", Arrays.asList(TrangThaiRapPhim.values()));
        // Danh sách nhân viên chưa quản lý rạp nào + 1 nhân viên đang quản lý của rạp hiện tại (nếu có)
        model.addAttribute("nhanVienList", nhanVienList != null ? nhanVienList : new ArrayList<>());
        return "EditCinema";
    }

    // Thực hiện update
    @PostMapping("/update/{id}")
    public String updateCinema(@ModelAttribute("rapPhim") RapPhim rapPhim, @PathVariable int id, RedirectAttributes redirectAttributes) {
        RapPhim rp = cinemaService.updateCinema(id, rapPhim);
        if(rp != null) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật rạp phim thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Cập nhật rạp phim thất bại");
        }
        return "redirect:/cinemas/";
    }

    private List<DoiTuongSuDung> getNhanVienListChoRap(RapPhim rapPhim) {
        // Lấy danh sách nhân viên chưa quản lý rạp nào
        List<DoiTuongSuDung> nhanVienList = doiTuongSuDungService.getNhanVienChuaCoRap();

        // Lấy nhân viên quản lý hiện tại của rạp
        DoiTuongSuDung currentNhanVien = rapPhim.getNhanVien();

        if(currentNhanVien != null) {
            if(nhanVienList == null) {
                nhanVienList = new ArrayList<>();
            }
            nhanVienList.add(currentNhanVien);
        }
        return nhanVienList;
    }

    // Xoá rạp
    @PostMapping("/delete/{id}")
    public String deleteCinema(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        boolean isSuccess = cinemaService.deleteCinema(id);
        if(isSuccess) {
            redirectAttributes.addFlashAttribute("message", "Xoá rạp phim thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Xoá rạp phim thất bại");
        }
        return "redirect:/cinemas/";
    }
}