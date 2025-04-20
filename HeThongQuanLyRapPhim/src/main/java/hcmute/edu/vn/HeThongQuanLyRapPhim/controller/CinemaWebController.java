package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiRapPhim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/cinema-manager")
public class CinemaWebController {

    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private DoiTuongSuDungService doiTuongSuDungService;

    // Hiển thị danh sách rạp phim
    @GetMapping("/list")
    public String showList(Model model) {
        // phải đặt attribute name giống với bên html á nha
        model.addAttribute("cinemas", cinemaService.getAllCinemas());
        // Đây là tên file html muốn trả về
        return "list-cinema";
    }

    // Hiển thị form điền khi thêm rạp
    @GetMapping("/show-add-form")
    public String showAddForm(Model model) {
        model.addAttribute("rapPhim", new RapPhim());
        model.addAttribute("trangThaiList", Arrays.asList(TrangThaiRapPhim.values()));
        List<DoiTuongSuDung> nhanVienList = doiTuongSuDungService.getNhanVienChuaCoRap();
        model.addAttribute("nhanVienList", nhanVienList != null ? nhanVienList : new ArrayList<>());
        return "add-cinema";
    }

    // Xử lý lưu rạp phim mới
    @PostMapping("/add-new-to-database")
    public String saveCinema(@Valid @ModelAttribute("rapPhim") RapPhim rapPhim,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("trangThaiList", Arrays.asList(TrangThaiRapPhim.values()));
            List<DoiTuongSuDung> nhanVienList = doiTuongSuDungService.getNhanVienChuaCoRap();
            model.addAttribute("nhanVienList", nhanVienList != null ? nhanVienList : new ArrayList<>());
            return "add-cinema";
        }

        if (rapPhim.getNhanVien() == null) {
            rapPhim.setNhanVien(null);
        }

        try {
            cinemaService.createCinema(rapPhim);
            redirectAttributes.addFlashAttribute("message", "Thêm rạp phim thành công");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Thêm rạp phim thất bại");
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/cinema-manager/list";
    }

    // Hiển thị form điền khi cập nhật rạp
    @GetMapping("/show-edit-form/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        RapPhim rapPhim = cinemaService.getCinemaById(id);

        if (rapPhim == null) {
            return "redirect:/cinema-manager/list"; // Redirect nếu không tìm thấy
        }

        // Lấy danh sách nhân viên chưa được phân công
        List<DoiTuongSuDung> nhanVienList = doiTuongSuDungService.getNhanVienChuaCoRap();

        // Nếu rạp phim đã có nhân viên, thêm nhân viên đó vào danh sách để hiển thị trong combobox
        DoiTuongSuDung currentNhanVien = rapPhim.getNhanVien();
        if (currentNhanVien != null) {
            // Kiểm tra xem nhân viên hiện tại có trong danh sách chưa, nếu chưa thì thêm vào
            if (nhanVienList == null) {
                nhanVienList = new ArrayList<>();
            }
            if (!nhanVienList.contains(currentNhanVien)) {
                nhanVienList.add(currentNhanVien);
            }
        }

        // Thêm các thuộc tính vào model
        model.addAttribute("rapPhim", rapPhim);
        model.addAttribute("trangThaiList", Arrays.asList(TrangThaiRapPhim.values()));
        model.addAttribute("nhanVienList", nhanVienList != null ? nhanVienList : new ArrayList<>());
        return "edit-cinema";
    }

    // Phương thức xử lý cập nhật rạp phim
    @PostMapping("/edit-by-id")
    public String updateCinema(@Valid @ModelAttribute("rapPhim") RapPhim rapPhim,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // Lấy danh sách nhân viên chưa được phân công
            List<DoiTuongSuDung> nhanVienList = doiTuongSuDungService.getNhanVienChuaCoRap();

            // Nếu rạp phim đã có nhân viên, thêm nhân viên đó vào danh sách
            DoiTuongSuDung currentNhanVien = rapPhim.getNhanVien();
            if (currentNhanVien != null) {
                if (nhanVienList == null) {
                    nhanVienList = new ArrayList<>();
                }
                if (!nhanVienList.contains(currentNhanVien)) {
                    nhanVienList.add(currentNhanVien);
                }
            }

            model.addAttribute("trangThaiList", Arrays.asList(TrangThaiRapPhim.values()));
            model.addAttribute("nhanVienList", nhanVienList != null ? nhanVienList : new ArrayList<>());
            return "edit-cinema";
        }

        // Nếu không chọn nhân viên, đặt nhanVien là null
        if (rapPhim.getNhanVien() == null) {
            rapPhim.setNhanVien(null);
        }

        try {
            RapPhim existingCinema = cinemaService.getCinemaById(rapPhim.getIdRapPhim());
            if (existingCinema == null) {
                redirectAttributes.addFlashAttribute("message", "Rạp phim không tồn tại");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/cinema-manager/list";
            }
            cinemaService.updateCinema(rapPhim.getIdRapPhim(), rapPhim);
            redirectAttributes.addFlashAttribute("message", "Cập nhật rạp phim thành công");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật rạp phim thất bại");
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/cinema-manager/list";
    }

    // Xử lý xóa rạp phim
    @GetMapping("/delete-by-id/{id}")
    public String deleteCinema(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            boolean isDeleted = cinemaService.deleteCinema(id);
            if (isDeleted) {
                redirectAttributes.addFlashAttribute("message", "Xóa rạp phim thành công");
                redirectAttributes.addFlashAttribute("messageType", "success");
            } else {
                redirectAttributes.addFlashAttribute("message", "Xóa rạp phim thất bại");
                redirectAttributes.addFlashAttribute("messageType", "error");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Xóa rạp phim thất bại");
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/cinema-manager/list";
    }
}
