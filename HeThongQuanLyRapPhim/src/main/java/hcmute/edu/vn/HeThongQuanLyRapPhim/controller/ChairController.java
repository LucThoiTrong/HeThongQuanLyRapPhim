package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DayGhe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Ghe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.ChairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/seats")
public class ChairController {

    private final ChairService chairService;

    @Autowired
    public ChairController(ChairService chairService) {
        this.chairService = chairService;
    }

    @GetMapping("/edit/{idRapPhim}/{idPhongChieuPhim}")
    public String manageChairs(@PathVariable("idRapPhim") int idRapPhim,
                               @PathVariable("idPhongChieuPhim") int idPhongChieuPhim,
                               Model model) {
        try {
            PhongChieuPhim phongChieuPhim = chairService.getPhongChieuPhimById(idPhongChieuPhim);
            // Chuyển từ Set sang List và sắp xếp theo tên dãy ghế
            List<DayGhe> dsDayGheList = new ArrayList<>(phongChieuPhim.getDsDayGhe());
            dsDayGheList.sort(Comparator.comparing(DayGhe::getTenDayGhe));

            model.addAttribute("phongChieuPhim", phongChieuPhim);
            model.addAttribute("dsDayGhe", dsDayGheList); // gán list đã sắp xếp
            model.addAttribute("idRapPhim", idRapPhim);
            return "ChairPage";
        } catch (RuntimeException e) {
            model.addAttribute("message", "Lỗi khi tải trang quản lý ghế: " + e.getMessage());
            return "redirect:/rows/" + idRapPhim + "/" + idPhongChieuPhim;
        }
    }


    @PostMapping("/update")
    public String updateChairStatus(@RequestParam("idGhe") int idGhe,
                                    @RequestParam("trangThaiGhe") boolean trangThaiGhe,
                                    @RequestParam("idRapPhim") int idRapPhim,
                                    @RequestParam("idPhongChieuPhim") int idPhongChieuPhim,
                                    RedirectAttributes redirectAttributes) {
        try {
            Ghe gheMoi = new Ghe();
            gheMoi.setTrangThaiGhe(trangThaiGhe);
            chairService.updateChair(idGhe, gheMoi);
            redirectAttributes.addFlashAttribute("message", "Cập nhật trạng thái ghế thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật trạng thái ghế thất bại: " + e.getMessage());
        }
        return "redirect:/seats/edit/" + idRapPhim + "/" + idPhongChieuPhim;
    }
}