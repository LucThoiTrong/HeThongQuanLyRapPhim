package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ChienDichGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.DiscountCampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/discount-campaign")
public class DiscountCampaignController {
    private final DiscountCampaignService chienDichGiamGiaService;

    @Autowired
    public DiscountCampaignController(DiscountCampaignService discountCampaignService) {
        this.chienDichGiamGiaService = discountCampaignService;
    }

    // Lấy danh sách chiến dịch giảm giá
    @GetMapping("/")
    public String list(Model model) {
        List<ChienDichGiamGia> chienDichGiamGiaList = chienDichGiamGiaService.findAll();
        model.addAttribute("chienDichGiamGiaList", chienDichGiamGiaList);
        return "DiscountCampaignPage";
    }

    // Thêm chiến dịch giảm giá
    @GetMapping("/themChienDich")
    public String themChienDich(Model model) {
        //tao model de lien ket du lieu tu form
        ChienDichGiamGia chienDichGiamGia = new ChienDichGiamGia();
        model.addAttribute("chienDichGiamGia", chienDichGiamGia);
        return "AddDiscountCampaignPage";
    }


    @GetMapping("/themDanhSachMaGiamGia")
    public String themDanhSachMaGiamGia(@ModelAttribute("chienDichGiamGia") ChienDichGiamGia chienDichGiamGia,
                                        Model model) {
        model.addAttribute("chienDichGiamGia", chienDichGiamGia);
        MaGiamGia maGiamGia = new MaGiamGia();
        model.addAttribute("maGiamGia", maGiamGia);
        model.addAttribute("soLuongMaGiamGia", 0);
        return "AddDiscountWithCampaignPage";
    }

    // Thực hiện thêm chiến dịch
    @PostMapping("/save")
    public String save(@RequestParam("tenChienDich") String tenChienDich,
                       @RequestParam("ngayBatDauChienDich") LocalDateTime ngayBatDau,
                       @RequestParam("ngayKetThucChienDich") LocalDateTime ngayKetThuc,
                       @ModelAttribute("maGiamGia") MaGiamGia maGiamGia,
                       @RequestParam("soLuongMaGiamGia") int soLuongMaGiamGia, RedirectAttributes redirectAttributes) {
        ChienDichGiamGia chienDichGiamGia = chienDichGiamGiaService.insert(tenChienDich, ngayBatDau, ngayKetThuc, maGiamGia, soLuongMaGiamGia);
        if(chienDichGiamGia != null) {
            redirectAttributes.addFlashAttribute("message", "Thêm chiến dịch thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Thêm chiến dịch thất bại");
        }
        return "redirect:/discount-campaign/";
    }

    @GetMapping("/HienFormDeCapNhat")
    public String showFormForUpdate(@RequestParam("idChienDichGiamGia") int theId, Model model) {
        ChienDichGiamGia chienDichGiamGia = chienDichGiamGiaService.findById(theId);
        model.addAttribute("chienDichGiamGia", chienDichGiamGia);
        return "EditDiscountCampaignPage";
    }

    // Thực hiện cập nhật
    @GetMapping("/capNhat")
    public String save(@RequestParam("idChienDichGiamGia") int id,
                       @RequestParam("tenChienDich") String tenChienDich,
                       @RequestParam("ngayBatDauChienDich") LocalDateTime ngayBatDau,
                       @RequestParam("ngayKetThucChienDich") LocalDateTime ngayKetThuc,
                       RedirectAttributes redirectAttributes) {
        ChienDichGiamGia result = chienDichGiamGiaService.update(id, tenChienDich, ngayBatDau, ngayKetThuc);
        if(result != null) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật chiến dịch thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Cập nhật chiến dịch thất bại");
        }
        return "redirect:/discount-campaign/";
    }

    // Thực hiện delete
    @GetMapping("/delete")
    public String delete(@RequestParam("idChienDichGiamGia") int theId, RedirectAttributes redirectAttributes) {
        boolean result = chienDichGiamGiaService.deleteById(theId);
        if(result) {
            redirectAttributes.addFlashAttribute("message", "Xoá chiến dịch thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Xoá chiến dịch thất bại");
        }
        return "redirect:/discount-campaign/";
    }
}
