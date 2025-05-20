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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

@Controller
@RequestMapping("/refund")
public class RefundController {
    private final InvoiceService invoiceService;

    private final RefundService refundService;

    private final ChairService chairService;

    private final UserService profileService;

    @Autowired
    public RefundController(InvoiceService invoiceService, RefundService refundService,
                            ChairService chairService, UserService profileService) {
        this.invoiceService = invoiceService;
        this.refundService = refundService;
        this.chairService = chairService;
        this.profileService = profileService;
    }
    //hien danh sach hoa don cua doi tuong do
//    @GetMapping("/danh-sach-hoa-don")
//    public String showInvoices(HttpSession session, Model model) {
//        // Kiểm tra đăng nhập
//        session.setAttribute("idCustomer",1);
//        int idCustomer = (int) session.getAttribute("idCustomer");
//        DoiTuongSuDung customer = (DoiTuongSuDung) session.getAttribute("user");
//        if (customer == null) {
//            return "redirect:/"; // Redirect về trang đăng nhập hoặc trang chủ
//        }
//        // Lấy danh sách hóa đơn
//        List<HoaDon> invoices = invoiceService.findByIdDoiTuongSuDung(idCustomer);
//        model.addAttribute("invoices", invoices);
//        model.addAttribute("pageTitle", "Your Invoices");
//        return "LichSuDatVe";
//    }
    @GetMapping("/yeu-cau-hoan-tra/{id}")
    public String yeuCauHoanTra(
            @PathVariable("id") int idHoaDon,
            HttpSession session, Model model) {
        DoiTuongSuDung customer = (DoiTuongSuDung) session.getAttribute("user");
        if (customer == null) {
            session.setAttribute("errorMessage", "Dang nhap de co the hoan tra ve.");
            //quay lai trang dang nhap
            return "";
        }
        HoaDon hoaDon = invoiceService.findById(idHoaDon);
        model.addAttribute("hoaDon", hoaDon);
        return "InvoiceDetailPage";
    }

    @GetMapping("/thuc-hien-hoan-tra/{id}")
    public String thucHienHoanTra(
            @PathVariable("id") int id,
            @RequestParam("lyDoHoanTra") String lyDoHoanTra,
            HttpSession session, Model model,
            RedirectAttributes redirectAttributes) {

        Integer idCustomer = (Integer) session.getAttribute("idCustomer");
        DoiTuongSuDung doiTuongSuDung;
        if (idCustomer == null) {
            session.setAttribute("errorMessage", "Vui lòng đăng nhập để thực hiện hoàn trả.");
            //quay lai trang dang nhap
            return "";
        }
        else {
            doiTuongSuDung = profileService.getDoiTuongSuDungById(idCustomer);
        }

        HoaDon hoaDon = invoiceService.findById(id);
        model.addAttribute("hoaDon", hoaDon);

        LocalDateTime ngayGioChieu = hoaDon.getSuatChieu().getNgayGioChieu();
        LocalDateTime now = LocalDateTime.now();

        //phim da bat dau chieu
        if (now.isAfter(ngayGioChieu)) {
            model.addAttribute("errorMessage", "Vé không thể hoàn trả do phim đã bắt đầu chiếu.");
            return "InvoiceDetailPage";
        }

        // con it hon 45p
        Duration duration = Duration.between(now, ngayGioChieu);
        if (duration.toMinutes() < 45) {
            model.addAttribute("errorMessage", "Vé không thể hoàn trả do đã quá thời gian cho phép.");
            return "InvoiceDetailPage";
        }
        HoanTra hoanTra = new HoanTra();
        hoanTra.setDoiTuongSuDung(doiTuongSuDung);
        hoanTra.setHoaDon(hoaDon);
        hoanTra.setNgayHoanTra(LocalDateTime.now());
        hoanTra.setLyDoHoanTra(lyDoHoanTra);

        Set<VeXemPhim> veSet = hoaDon.getDsVeXemPhimDaMua();
        StringBuilder sb = new StringBuilder();

        for (VeXemPhim ve : veSet) {
            sb.append(ve.getGhe().getIdGhe()).append(",");
        }

        // Xoá dấu phẩy cuối
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }

        String danhSachIdGhe = sb.toString();
        // Thực hiện hoàn trả
        chairService.capNhatTrangThaiGhe(danhSachIdGhe,false);
        hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.DA_HOAN_TRA);
        refundService.save(hoanTra);
        redirectAttributes.addFlashAttribute("successMessage","Hoàn ve thanh cong, so tien se duoc hoan vao tai khoan cua ban!");
        return "redirect:/refund/danh-sach-hoa-don";
    }

}
