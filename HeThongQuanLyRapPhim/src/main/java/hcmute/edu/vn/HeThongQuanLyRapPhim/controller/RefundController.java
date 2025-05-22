package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.*;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/refund")
public class RefundController {
    private final RefundService refundService;

    @Autowired
    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @GetMapping("/yeu-cau-hoan-tra/{id}")
    public String yeuCauHoanTra(
            @PathVariable("id") int idHoaDon,
            HttpSession session, Model model) {
        DoiTuongSuDung customer = (DoiTuongSuDung) session.getAttribute("user");
        if (customer == null) {
            session.setAttribute("errorMessage", "Đăng nhập để có thể hoàn trả vé");
            //quay lai trang dang nhap
            return "LoginPage";
        }
        HoaDon hoaDon = refundService.getHoaDon(idHoaDon);
        model.addAttribute("hoaDon", hoaDon);
        return "InvoiceDetailPage";
    }

    @GetMapping("/thuc-hien-hoan-tra/{id}")
    public String thucHienHoanTra(
            @PathVariable("id") int id,
            @RequestParam("lyDoHoanTra") String lyDoHoanTra,
            HttpSession session, Model model,
            RedirectAttributes redirectAttributes) {

        DoiTuongSuDung customer = (DoiTuongSuDung) session.getAttribute("user");

        if (customer == null) {
            session.setAttribute("errorMessage", "Vui lòng đăng nhập để thực hiện hoàn trả.");
            //quay lai trang dang nhap
            return "LoginPage";
        }

        HoaDon hoaDon = refundService.getHoaDon(id);
        model.addAttribute("hoaDon", hoaDon);

        String error = refundService.checkHoaDon(hoaDon);
        if(!error.isEmpty()) {
            model.addAttribute("errorMessage", error);
            return "InvoiceDetailPage";
        }

        HoanTra hoanTra = refundService.save(hoaDon, customer, lyDoHoanTra);
        if (hoanTra == null) {
            redirectAttributes.addFlashAttribute("errorMessage","Hoàn vé thất bại");
        } else {
            redirectAttributes.addFlashAttribute("successMessage","Hoàn vé thành công");
        }
        return "redirect:/user/profile";
    }

}
