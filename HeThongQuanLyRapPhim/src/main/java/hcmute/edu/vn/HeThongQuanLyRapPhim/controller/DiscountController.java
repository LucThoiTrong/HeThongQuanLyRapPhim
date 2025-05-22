package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ChienDichGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/discount")
public class DiscountController {
    private final DiscountService discountService;

    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping("/list")
    public String getDiscountList(Model model) {
        List<MaGiamGia> maGiamGiaList = discountService.findAll();
        model.addAttribute("maGiamGiaList", maGiamGiaList);
        return "DiscountPage";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        List<ChienDichGiamGia> chienDichGiamGiaList = discountService.findAllChienDichGiamGia();
        model.addAttribute("listChienDichGiamGia", chienDichGiamGiaList);
        model.addAttribute("maGiamGia", new MaGiamGia());
        return "AddDiscountPage";
    }

    // Thực hiện thêm mã giảm giá
    @PostMapping("/save")
    public String saveMaGiamGia(@ModelAttribute MaGiamGia maGiamGia, RedirectAttributes redirectAttributes) {
        MaGiamGia result = discountService.insert(maGiamGia);
        if(result != null) {
            redirectAttributes.addFlashAttribute("message", "Thêm mã giảm giá thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Thêm mã giảm giá thất bại");
        }
        return "redirect:/discount/list";
    }

    @GetMapping("/HienFormDeCapNhat")
    public String showEditForm(@RequestParam("idMaGiamGia") int id, Model model) {
        List<ChienDichGiamGia> chienDichGiamGiaList = discountService.findAllChienDichGiamGia();
        model.addAttribute("listChienDichGiamGia", chienDichGiamGiaList);
        MaGiamGia maGiamGia = discountService.findById(id);
        model.addAttribute("maGiamGia", maGiamGia);
        return "EditDiscountPage";
    }

    // Thực hiện update
    @PostMapping("/update")
    public String updateMaGiamGia(@RequestParam("idMaGiamGia") int id,@ModelAttribute MaGiamGia maGiamGia, RedirectAttributes redirectAttributes) {
        MaGiamGia result = discountService.update(id, maGiamGia);
        if(result != null) {
            redirectAttributes.addFlashAttribute("message", "Thêm mã giảm giá thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Thêm mã giảm giá thất bại");
        }
        return "redirect:/discount/list";
    }

    @GetMapping("/delete")
    public String deleteMaGiamGia(@RequestParam("idMaGiamGia") int id) {
        discountService.deleteById(id);
        return "redirect:/discount/list";
    }
}
