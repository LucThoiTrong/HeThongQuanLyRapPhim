package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ChienDichGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.DiscountCampaignService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/discount")
public class DiscountController {
    private final DiscountService discountService;
    private final DiscountCampaignService discountCampaignService;

    @Autowired
    public DiscountController(DiscountService discountService, DiscountCampaignService discountCampaignService) {
        this.discountService = discountService;
        this.discountCampaignService = discountCampaignService;
    }

    @GetMapping("/list")
    public String getDiscountList(Model model) {
        List<MaGiamGia> maGiamGiaList = discountService.findAll();
        model.addAttribute("maGiamGiaList", maGiamGiaList);
        return "DiscountPage";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        List<ChienDichGiamGia> chienDichGiamGiaList = discountCampaignService.findAll();
        model.addAttribute("listChienDichGiamGia", chienDichGiamGiaList);
        model.addAttribute("maGiamGia", new MaGiamGia());
        return "AddDiscountPage"; // Tên file form tạo mới: resources/templates/discount/create.html
    }

    @PostMapping("/save")
    public String saveMaGiamGia(@ModelAttribute MaGiamGia maGiamGia) {
        discountService.save(maGiamGia);
        return "redirect:/discount/list"; // Sau khi lưu thì chuyển về trang danh sách
    }

    @GetMapping("/HienFormDeCapNhat")
    public String showEditForm(@RequestParam("idMaGiamGia") int id, Model model) {
        List<ChienDichGiamGia> chienDichGiamGiaList = discountCampaignService.findAll();
        model.addAttribute("listChienDichGiamGia", chienDichGiamGiaList);
        MaGiamGia maGiamGia = discountService.findById(id);
        model.addAttribute("maGiamGia", maGiamGia);
        return "AddDiscountPage"; // Tên file form chỉnh sửa: resources/templates/discount/edit.html
    }

    @GetMapping("/delete")
    public String deleteMaGiamGia(@RequestParam("idMaGiamGia") int id) {
        discountService.deleteById(id);
        return "redirect:/discount/list";
    }
}
