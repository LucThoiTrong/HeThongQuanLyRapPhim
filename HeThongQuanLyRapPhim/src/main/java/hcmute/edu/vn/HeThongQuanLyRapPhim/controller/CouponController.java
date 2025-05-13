package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.DoiTuongSuDungService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.DiscountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CouponController {
    private final DiscountService discountService;
    private final DoiTuongSuDungService doiTuongSuDungService;

    @Autowired
    public CouponController(DiscountService discountService, DoiTuongSuDungService doiTuongSuDungService) {
        this.discountService = discountService;
        this.doiTuongSuDungService = doiTuongSuDungService;
    }

    @GetMapping("/coupon")
    public String showCouponPage(Model model) {
        List<MaGiamGia> maGiamGiaList = discountService.findAll();
        model.addAttribute("danhSachMaGiamGia", maGiamGiaList);
        return "CouponPage";
    }

    @PostMapping("/coupon/{idMaGiamGia}/collect")
    public String collectDiscount(Model model, @PathVariable int idMaGiamGia, HttpSession session, RedirectAttributes redirectAttributes) {
        int idCustomer = (int) session.getAttribute("idCustomer");

        DoiTuongSuDung customer = doiTuongSuDungService.getDoiTuongSuDungById(idCustomer);
        MaGiamGia maGiamGia = discountService.findById(idMaGiamGia);
        maGiamGia.setDoiTuongSuDung(customer);

        MaGiamGia kq = discountService.updateCustomer(maGiamGia);
        if(kq != null) {
            redirectAttributes.addFlashAttribute("message", "Thu thập mã giảm giá thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Thu thập mã giả thất bại");
        }
        return "redirect:/coupon";
    }
}
