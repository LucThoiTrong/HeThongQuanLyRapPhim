package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.DoiTuongSuDungService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MaGiamGiaService;
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
    private final MaGiamGiaService maGiamGiaService;
    private final DoiTuongSuDungService doiTuongSuDungService;

    @Autowired
    public CouponController(MaGiamGiaService maGiamGiaService, DoiTuongSuDungService doiTuongSuDungService) {
        this.maGiamGiaService = maGiamGiaService;
        this.doiTuongSuDungService = doiTuongSuDungService;
    }

    @GetMapping("/coupon")
    public String showCouponPage(Model model) {
        List<MaGiamGia> maGiamGiaList = maGiamGiaService.findAll();
        model.addAttribute("danhSachMaGiamGia", maGiamGiaList);
        return "CouponPage";
    }

    @PostMapping("/coupon/{idMaGiamGia}/collect")
    public String collectDiscount(Model model, @PathVariable int idMaGiamGia, HttpSession session, RedirectAttributes redirectAttributes) {
        int idCustomer = (int) session.getAttribute("idCustomer");

        DoiTuongSuDung customer = doiTuongSuDungService.getDoiTuongSuDungById(idCustomer);
        MaGiamGia maGiamGia = maGiamGiaService.findById(idMaGiamGia);
        maGiamGia.setDoiTuongSuDung(customer);

        MaGiamGia kq = maGiamGiaService.updateCustomer(maGiamGia);
        if(kq != null) {
            redirectAttributes.addFlashAttribute("message", "Thu thập mã giảm giá thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Thu thập mã giả thất bại");
        }
        return "redirect:/coupon";
    }
}
