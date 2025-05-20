package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DayGhe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.LoaiGhe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.RowChairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.MessageFormat;
import java.util.List;

@Controller
@RequestMapping("/rows")
public class RowChairController {

    private final RowChairService rowChairService;

    @Autowired
    public RowChairController(RowChairService rowChairService) {
        this.rowChairService = rowChairService;
    }

    // Hiển thị danh sách dãy ghế
    @GetMapping("/{idRapPhim}/{idPhongChieuPhim}")
    public String showList(Model model, @PathVariable("idRapPhim") int idRapPhim,
                           @PathVariable("idPhongChieuPhim") int idPhongChieuPhim) {
        PhongChieuPhim phongChieuPhim = rowChairService.getRoomById(idPhongChieuPhim);

        List<DayGhe> dsDayGhe = rowChairService.getAllRowChairByIdRoom(idPhongChieuPhim);

        model.addAttribute("dsDayGhe", dsDayGhe);
        model.addAttribute("phongChieuPhim", phongChieuPhim);
        model.addAttribute("idRapPhim", idRapPhim);
        return "RowChairPage";
    }

    // Hiển thị form cập nhật dãy ghế
    @GetMapping("/update/{idRapPhim}/{idPhongChieuPhim}")
    public String showUpdateForm(Model model, @PathVariable("idRapPhim") int idRapPhim,
                                 @PathVariable("idPhongChieuPhim") int idPhongChieuPhim) {
        PhongChieuPhim phongChieuPhim = rowChairService.getRoomById(idPhongChieuPhim);

        int soLuongDoi = phongChieuPhim.getSoLuongDayGheTungLoai(LoaiGhe.DOI);
        int soLuongVip = phongChieuPhim.getSoLuongDayGheTungLoai(LoaiGhe.VIP);
        int soLuongThuong = phongChieuPhim.getSoLuongDayGheTungLoai(LoaiGhe.THUONG);

        model.addAttribute("soLuongDoi", soLuongDoi);
        model.addAttribute("soLuongVip", soLuongVip);
        model.addAttribute("soLuongThuong", soLuongThuong);
        model.addAttribute("phongChieuPhim", phongChieuPhim);
        model.addAttribute("idRapPhim", idRapPhim);
        model.addAttribute("maxRows", phongChieuPhim.getSoLuongDayGheToiDa());

        return "EditRowChairPage";
    }

    // Xử lý cập nhật dãy ghế
    @PostMapping("/update/{idRapPhim}/{idPhongChieuPhim}")
    public String updateRowChairs(@PathVariable("idRapPhim") int idRapPhim,
                                  @PathVariable("idPhongChieuPhim") int idPhongChieuPhim,
                                  @RequestParam("soLuongDoi") int soLuongDoi,
                                  @RequestParam("soLuongVip") int soLuongVip,
                                  @RequestParam("soLuongThuong") int soLuongThuong,
                                  RedirectAttributes redirectAttributes) {

        PhongChieuPhim phongChieuPhim = rowChairService.getRoomById(idPhongChieuPhim);

        int totalRows = soLuongDoi + soLuongVip + soLuongThuong;
        int maxRows = phongChieuPhim.getSoLuongDayGheToiDa();

        if (totalRows != maxRows) {
            redirectAttributes.addFlashAttribute("message", "Tổng số dãy ghế phải bằng " + maxRows + "!");
            return MessageFormat.format("redirect:/rows/update/{0}/{1}", idRapPhim, idPhongChieuPhim);
        }
        rowChairService.updateRowChair(phongChieuPhim, soLuongDoi, soLuongVip, soLuongThuong);
        redirectAttributes.addFlashAttribute("message", "Cập nhật dãy ghế thành công!");

        return "redirect:/rows/" + idRapPhim + "/" + idPhongChieuPhim;
    }
}