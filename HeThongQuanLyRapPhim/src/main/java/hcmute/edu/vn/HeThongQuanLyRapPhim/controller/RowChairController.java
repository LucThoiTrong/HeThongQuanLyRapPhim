package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DayGhe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.KichThuocPhong;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.LoaiGhe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.RowChairService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                           @PathVariable("idPhongChieuPhim") int idPhongChieuPhim,
                           HttpSession session, RedirectAttributes redirectAttributes) {
        PhongChieuPhim phongChieuPhim = rowChairService.getRoomById(idPhongChieuPhim);
        if (phongChieuPhim == null || phongChieuPhim.getKichThuocPhong() == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng chọn kích thước phòng trước khi quản lý dãy ghế!");
            return "redirect:/rooms/update/" + idPhongChieuPhim + "?idRapPhim=" + idRapPhim;
        }

        List<DayGhe> dsDayGhe = rowChairService.getAllRowChairByIdRoom(idPhongChieuPhim);
        session.setAttribute("dsDayGhe", dsDayGhe);
        model.addAttribute("dsDayGhe", dsDayGhe);
        model.addAttribute("phongChieuPhim", phongChieuPhim);
        model.addAttribute("idRapPhim", idRapPhim);
        return "RowChairListPage";
    }

    // Hiển thị form cập nhật dãy ghế
    @GetMapping("/update/{idRapPhim}/{idPhongChieuPhim}")
    public String showUpdateForm(Model model, @PathVariable("idRapPhim") int idRapPhim,
                                 @PathVariable("idPhongChieuPhim") int idPhongChieuPhim,
                                 HttpSession session, RedirectAttributes redirectAttributes) {
        PhongChieuPhim phongChieuPhim = rowChairService.getRoomById(idPhongChieuPhim);
        if (phongChieuPhim == null || phongChieuPhim.getKichThuocPhong() == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng chọn kích thước phòng trước khi quản lý dãy ghế!");
            return "redirect:/rooms/update/" + idPhongChieuPhim + "?idRapPhim=" + idRapPhim;
        }

        List<DayGhe> dsDayGhe = (List<DayGhe>) session.getAttribute("dsDayGhe");
        if (dsDayGhe == null) {
            dsDayGhe = rowChairService.getAllRowChairByIdRoom(idPhongChieuPhim);
        }

        long soLuongDoi = dsDayGhe.stream().filter(dayGhe -> dayGhe.getLoaiGhe() == LoaiGhe.DOI).count();
        long soLuongVip = dsDayGhe.stream().filter(dayGhe -> dayGhe.getLoaiGhe() == LoaiGhe.VIP).count();
        long soLuongThuong = dsDayGhe.stream().filter(dayGhe -> dayGhe.getLoaiGhe() == LoaiGhe.THUONG).count();

        model.addAttribute("soLuongDoi", soLuongDoi);
        model.addAttribute("soLuongVip", soLuongVip);
        model.addAttribute("soLuongThuong", soLuongThuong);
        model.addAttribute("phongChieuPhim", phongChieuPhim);
        model.addAttribute("idRapPhim", idRapPhim);
        model.addAttribute("maxRows", getMaxRows(phongChieuPhim.getKichThuocPhong()));

        return "EditRowChair";
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
        if (phongChieuPhim == null || phongChieuPhim.getKichThuocPhong() == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng chọn kích thước phòng trước khi quản lý dãy ghế!");
            return "redirect:/rooms/update/" + idPhongChieuPhim + "?idRapPhim=" + idRapPhim;
        }

        int totalRows = soLuongDoi + soLuongVip + soLuongThuong;
        int maxRows = getMaxRows(phongChieuPhim.getKichThuocPhong());
        if (totalRows != maxRows) {
            redirectAttributes.addFlashAttribute("message", "Tổng số dãy ghế phải bằng " + maxRows + "!");
            return "redirect:/rows/update/" + idRapPhim + "/" + idPhongChieuPhim;
        }

        try {
            rowChairService.updateRowChair(phongChieuPhim, soLuongDoi, soLuongVip, soLuongThuong);
            redirectAttributes.addFlashAttribute("message", "Cập nhật dãy ghế thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật dãy ghế thất bại: " + e.getMessage());
        }
        return "redirect:/rows/" + idRapPhim + "/" + idPhongChieuPhim;
    }

    private int getMaxRows(KichThuocPhong kichThuocPhong) {
        if (kichThuocPhong == null) {
            return 0;
        }
        switch (kichThuocPhong) {
            case NHO:
                return 10;
            case VUA:
                return 15;
            case LON:
                return 20;
            default:
                return 0;
        }
    }
}