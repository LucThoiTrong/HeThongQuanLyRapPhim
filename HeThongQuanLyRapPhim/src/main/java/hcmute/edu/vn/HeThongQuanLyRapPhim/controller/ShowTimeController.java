package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HinhThucChieu;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.SuatChieu;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.ShowTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/showtimes")
public class ShowTimeController {
    private final ShowTimeService showTimeService;

    @Autowired
    public ShowTimeController(ShowTimeService showTimeService) {
        this.showTimeService = showTimeService;
    }

    // Hiển thị danh sách suất chiếu
    @GetMapping("/")
    public String showList(Model model) {
        List<SuatChieu> dsSuatChieu = showTimeService.getAllShowTimes();
        model.addAttribute("showtimes", dsSuatChieu);
        return "ShowTimeListPage";
    }

    // Lấy danh sách các phòng chiếu theo ra phim được chọn
    @GetMapping("/rap-phim/{idRapPhim}/phong-chieu")
    @ResponseBody
    public ResponseEntity<List<PhongChieuPhim>> getRoomsByCinemaId(@PathVariable int idRapPhim) {
        List<PhongChieuPhim> phongChieuPhimList = showTimeService.getAllPhongChieuPhims(idRapPhim);
        return ResponseEntity.ok(phongChieuPhimList);
    }

    // Hiển thị form thêm suất chiếu
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("suatChieu", new SuatChieu());
        model.addAttribute("hinhThucChieuList", Arrays.asList(HinhThucChieu.values()));
        model.addAttribute("phimList", showTimeService.getAllPhims());
        model.addAttribute("rapPhimList", showTimeService.getAllRapPhims());
        return "AddShowTimePage";
    }

    // Xử lý thêm suất chiếu
    @PostMapping("/new")
    public String insertShowTime(@ModelAttribute("suatChieu") SuatChieu suatChieu, RedirectAttributes redirectAttributes) {
        if(showTimeService.kiemTraThoiGianSuatChieu(suatChieu)) {
            redirectAttributes.addFlashAttribute("message", "Thời gian suất chiếu bị trùng vui lòng kiểm tra lại");
            return "redirect:/showtimes/new";
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
    public String showEditForm(@PathVariable("id") int id, Model model) {
        // Lấy các thông tin của suất chiếu
        SuatChieu suatChieu = showTimeService.getShowTimeById(id);

        int idRapPhim = suatChieu.getPhongChieuPhim().getRapPhim().getIdRapPhim();
        List<PhongChieuPhim> phongChieuList = showTimeService.getAllPhongChieuPhims(idRapPhim);

        model.addAttribute("suatChieu", suatChieu);
        model.addAttribute("hinhThucChieuList", Arrays.asList(HinhThucChieu.values()));
        model.addAttribute("phimList", showTimeService.getAllPhims());
        model.addAttribute("rapPhimList", showTimeService.getAllRapPhims());
        model.addAttribute("phongChieuList", phongChieuList);
        model.addAttribute("selectedRapPhimId", idRapPhim);
        return "EditShowTimePage";
    }

    // Xử lý cập nhật suất chiếu
    @PostMapping("/update/{id}")
    public String updateShowTime(@ModelAttribute("suatChieu") SuatChieu suatChieu, @PathVariable int id, RedirectAttributes redirectAttributes) {
        if(showTimeService.kiemTraThoiGianSuatChieu(suatChieu)) {
            redirectAttributes.addFlashAttribute("message", "Thời gian suất chiếu bị trùng vui lòng kiểm tra lại");
            return "redirect:/showtimes/new";
        }
        SuatChieu result = showTimeService.updateShowTime(id, suatChieu);
        if (result != null) {
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
}