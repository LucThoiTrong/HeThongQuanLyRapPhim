package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.*;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    private final VNPayService vnpayService;
    private final InvoiceService hoaDonService;
    private final ChairService chairService;
    private final PopcornDrinkComboService popcornDrinkComboService;
    private final EmailService emailService;

    @Autowired
    public PaymentController(VNPayService vnpayService, InvoiceService hoaDonService,
                             ChairService chairService,PopcornDrinkComboService popcornDrinkComboService,
                             EmailService emailService) {
        this.vnpayService = vnpayService;
        this.hoaDonService = hoaDonService;
        this.chairService = chairService;
        this.popcornDrinkComboService = popcornDrinkComboService;
        this.emailService = emailService;
    }

    @SuppressWarnings("SpringMVCViewInspection")
    @PostMapping("/vnpay")
    public String createPayment(@RequestParam("tongTienSauGiam") double amount,
                                Model model, HttpSession session,HttpServletRequest request) {
        MaGiamGia maGiamGia = (MaGiamGia) session.getAttribute("maGiamGiaDaChon");

        // Thực hiện cập nhật trạng thái mã giảm giá đã sử dụng
        if (maGiamGia != null) {
            maGiamGia.setTrangThaiSuDung(true);
            vnpayService.saveMaGiamGiaApDung(maGiamGia);
        }

        String clientIp = getClientIpAddress(request);

        int amountInt = (int) amount;
        try {
            String paymentUrl = vnpayService.createPayment(amountInt,clientIp);
            return "redirect:" + paymentUrl; // chuyển hướng đến URL thanh toán
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "vnpay";
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi khi tạo thanh toán!");
            return "vnpay";
        }
    }

    @GetMapping("/vnpay-return")
    public String handlePaymentReturn(@RequestParam("vnp_ResponseCode") String responseCode,
                                      Model model, HttpSession session) {
        String message = vnpayService.getPaymentMessage(responseCode);

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

            // Tạo hoá đơn
            HoaDon hoaDon = new HoaDon();

            Set<VeXemPhim> dsVeXemPhim = new HashSet<>();
            if (danhSachGhe != null && !danhSachGhe.isEmpty()) {
                String[] gheArray = danhSachGhe.split(",");
                for (String id : gheArray) {
                    // Xoá tất cả ký tự không phải là số
                    String soGhe = id.replaceAll("[^0-9]", "");  // giữ lại chỉ chữ số
                    if (!soGhe.isEmpty()) {
                        Ghe ghe = chairService.getChairById(Integer.parseInt(soGhe));
                        VeXemPhim veXemPhim = new VeXemPhim();
                        veXemPhim.setGhe(ghe);
                        veXemPhim.setSuatChieu(suatChieu);
                        veXemPhim.setHoaDon(hoaDon);
                        dsVeXemPhim.add(veXemPhim);
                    }
                }
            }

            Set<ChiTietComBoBapNuoc> dsChiTietComBoBapNuoc = new HashSet<>();
            @SuppressWarnings("unchecked") Map<Integer, Integer> comboSoLuong = (Map<Integer, Integer>) session.getAttribute("danhSachComboDuocChon");
            if (comboSoLuong != null) {
                for (Map.Entry<Integer, Integer> entry : comboSoLuong.entrySet()) {
                    Integer idCombo = entry.getKey();
                    Integer soLuong = entry.getValue();
                    ComboBapNuoc comboBapNuoc = popcornDrinkComboService.findById(idCombo);
                    ChiTietComBoBapNuoc chiTietComBoBapNuoc = new ChiTietComBoBapNuoc();
                    chiTietComBoBapNuoc.setComboBapNuoc(comboBapNuoc);
                    chiTietComBoBapNuoc.setSoLuong(soLuong);
                    chiTietComBoBapNuoc.setHoaDon(hoaDon);
                    dsChiTietComBoBapNuoc.add(chiTietComBoBapNuoc);
                }
            }
            hoaDon.setDoiTuongSuDung(doiTuongSuDung);
            hoaDon.setSuatChieu(suatChieu);
            hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.DA_THANH_TOAN);
            hoaDon.setDsVeXemPhimDaMua(dsVeXemPhim);
            hoaDon.setTongGiaTien(tongTienSauGiam);
            hoaDon.setDsComBoDaMua(dsChiTietComBoBapNuoc);
            hoaDon.setNgayThanhToan(LocalDateTime.now());
            hoaDonService.save(hoaDon);

            // Gửi email hóa đơn
            try {
                emailService.guiHoaDonQuaEmail(
                        doiTuongSuDung.getEmail(),
                        hoaDon
                );
            } catch (Exception e) {
                System.err.println("Lỗi gửi email hóa đơn: " + e.getMessage());
            }
        }
        model.addAttribute("message", message);
        return "AfterPaymentPage";
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        } else {
            // Nếu có nhiều IP, lấy cái đầu tiên
            ip = ip.split(",")[0];
        }
        return ip;
    }
}