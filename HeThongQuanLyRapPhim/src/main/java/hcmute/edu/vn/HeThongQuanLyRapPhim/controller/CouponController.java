package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MaGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CouponController {
    private final MaGiamGiaService maGiamGiaService;

    @Autowired
    public CouponController(MaGiamGiaService maGiamGiaService) {
        this.maGiamGiaService = maGiamGiaService;
    }

    @GetMapping("/coupon")
    public String showCouponPage(Model model) {
        List<MaGiamGia> maGiamGiaList = maGiamGiaService.findAll();
        model.addAttribute("danhSachMaGiamGia", maGiamGiaList);
        return "CouponPage";
    }
}
