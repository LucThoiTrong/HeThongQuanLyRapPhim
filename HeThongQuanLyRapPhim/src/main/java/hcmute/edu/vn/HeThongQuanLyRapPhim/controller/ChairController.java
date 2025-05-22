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

import java.text.MessageFormat;
import java.util.List;

@Controller
@RequestMapping("/seats")
public class ChairController {

    private final ChairService chairService;

    @Autowired
    public ChairController(ChairService chairService) {
        this.chairService = chairService;
    }

    // Hiện danh sách ghế
    @GetMapping("/edit/{idRapPhim}/{idPhongChieuPhim}")
    public String manageChairs(@PathVariable("idRapPhim") int idRapPhim,
                               @PathVariable("idPhongChieuPhim") int idPhongChieuPhim,
                               Model model) {
        PhongChieuPhim phongChieuPhim = chairService.getPhongChieuPhimById(idPhongChieuPhim);
        // Lấy toàn bộ danh sách dãy ghế trong phòng chiếu
        List<DayGhe> dsDayGheList = phongChieuPhim.getDsDayGheSorted();

        model.addAttribute("phongChieuPhim", phongChieuPhim);
        model.addAttribute("dsDayGhe", dsDayGheList);
        model.addAttribute("idRapPhim", idRapPhim);
        return "ChairPage";
    }

    @PostMapping("/update")
    public String updateChairStatus(@RequestParam("idGhe") int idGhe,
                                    @RequestParam("trangThaiGhe") boolean trangThaiGhe,
                                    @RequestParam("idRapPhim") int idRapPhim,
                                    @RequestParam("idPhongChieuPhim") int idPhongChieuPhim,
                                    RedirectAttributes redirectAttributes) {
        Ghe gheMoi = new Ghe();
        gheMoi.setTrangThaiGhe(trangThaiGhe);
        Ghe result = chairService.updateChair(idGhe, gheMoi);
        if (result != null) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật trạng thái ghế thành công!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Cập nhật trạng thái ghế thất bại");
        }
        return MessageFormat.format("redirect:/seats/edit/{0}/{1}", idRapPhim, idPhongChieuPhim);
    }
}