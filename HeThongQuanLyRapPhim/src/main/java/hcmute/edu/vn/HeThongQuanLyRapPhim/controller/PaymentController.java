package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.SuatChieu;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TKDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.strategy.PaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;
    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @PostMapping
    public String createPayment(@RequestParam("tongTienSauGiam") double amount,
                                @RequestParam("paymentMethod") String method,
                                Model model, HttpSession session) {
        MaGiamGia maGiamGia = (MaGiamGia) session.getAttribute("maGiamGiaDaChon");
        session.setAttribute("paymentMethod", method);
        // Thực hiện cập nhật trạng thái mã giảm giá đã sử dụng
        if (maGiamGia != null) {
            maGiamGia.setTrangThaiSuDung(true);
            paymentService.saveMaGiamGiaApDung(method,maGiamGia);
        }
        //Tạo yêu cầu thanh toán (Template Method bên trong từng PaymentService)
        try {
            String paymentUrl = paymentService.processPayment(method,amount);
            return "redirect:" + paymentUrl;  // chuyển hướng
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi khi tạo yêu cầu thanh toán: " + e.getMessage());
            return "AfterPaymentPage";
        }
    }
    @GetMapping("/momo-return")
    public String handleMomoReturn(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        String resultCode = params.get("resultCode");
        if ("0".equals(resultCode)) {
            String method = (String) session.getAttribute("paymentMethod");
            //lay cac thong tin tu session
            SuatChieu suatChieu = (SuatChieu) session.getAttribute("suatChieu");
            DoiTuongSuDung doiTuongSuDung = (DoiTuongSuDung) session.getAttribute("user");
            String danhSachGhe = (String) session.getAttribute("danhSachGheDuocChonIds");
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> comboSoLuong = (Map<Integer, Integer>) session.getAttribute("danhSachComboDuocChon");
            double tongTienSauGiam = (Double) session.getAttribute("tongTienSauGiam");
            paymentService.createInvoice(method, danhSachGhe, comboSoLuong, suatChieu, doiTuongSuDung, tongTienSauGiam);
            model.addAttribute("message", "Thanh toán thành công!");
        } else {
            model.addAttribute("message", "Thanh toán thất bại!");
        }
        return "AfterPaymentPage";
    }
    @GetMapping("/vnpay-return")
    public String handlePaymentReturn(@RequestParam("vnp_ResponseCode") String responseCode,
                                      Model model, HttpSession session) {
        String method = (String) session.getAttribute("paymentMethod");
        // Lấy service tương ứng theo phương thức thanh toán (Factory)
        // thanh toan thanh cong -> tao hoa don
        if ("00".equals(responseCode)) {
            SuatChieu suatChieu = (SuatChieu) session.getAttribute("suatChieu");
            double tongTienSauGiam = (Double) session.getAttribute("tongTienSauGiam");
            DoiTuongSuDung doiTuongSuDung = (DoiTuongSuDung) session.getAttribute("user");

            if (doiTuongSuDung == null) {
                model.addAttribute("taiKhoan", new TKDoiTuongSuDung());
                return "LoginPage";
            }

            String danhSachGhe = (String) session.getAttribute("danhSachGheDuocChonIds");
            @SuppressWarnings("unchecked") Map<Integer, Integer> comboSoLuong = (Map<Integer, Integer>) session.getAttribute("danhSachComboDuocChon");
            paymentService.createInvoice(method, danhSachGhe, comboSoLuong, suatChieu, doiTuongSuDung, tongTienSauGiam);
            model.addAttribute("message", "Thanh toán thành công!");
        }
        else {
            model.addAttribute("message", "Thanh toán thất bại!");
        }
        return "AfterPaymentPage";
    }
}