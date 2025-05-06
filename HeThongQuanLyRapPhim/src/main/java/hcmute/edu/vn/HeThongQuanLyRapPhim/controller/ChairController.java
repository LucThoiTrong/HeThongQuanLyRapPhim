package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DayGhe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Ghe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;

import hcmute.edu.vn.HeThongQuanLyRapPhim.service.ChairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

@Controller
@RequestMapping("/seats")
public class ChairController {

    @Autowired
    private ChairService chairService;

    @GetMapping("/edit/{idRapPhim}/{idPhongChieuPhim}")
    public String manageChairs(@PathVariable("idRapPhim") int idRapPhim,
                               @PathVariable("idPhongChieuPhim") int idPhongChieuPhim,
                               Model model) {
        try {
            PhongChieuPhim phongChieuPhim = chairService.getPhongChieuPhimById(idPhongChieuPhim);
            List<DayGhe> dsDayGheList = new ArrayList<>(phongChieuPhim.getDsDayGhe());
            dsDayGheList.sort(Comparator.comparing(DayGhe::getTenDayGhe));

            // Sắp xếp dsGhe trong mỗi DayGhe
            for (DayGhe dayGhe : dsDayGheList) {
                List<Ghe> dsGheList = new ArrayList<>(dayGhe.getDsGhe());
                dsGheList.sort(Comparator.comparing(ghe -> dayGhe.getTenDayGhe() + ghe.getIdGhe())); // Sắp xếp theo tên dãy + idGhe
                dayGhe.setDsGhe(new LinkedHashSet<>(dsGheList)); // Chuyển lại thành Set để lưu trữ
            }

            model.addAttribute("phongChieuPhim", phongChieuPhim);
            model.addAttribute("dsDayGhe", dsDayGheList);
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