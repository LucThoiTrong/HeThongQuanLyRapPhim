package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.*;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/booking")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    //tim suat chieu cua phim theo ngay chieu va hinh thuc chieu
    @GetMapping("/{idPhim}")
    public String getSuatChieuTheoPhim(@PathVariable int idPhim,
                                       @RequestParam(required = false) LocalDate ngayChieu,
                                       @RequestParam(required = false) HinhThucChieu hinhThucChieu,
                                       Model model, HttpSession session) {

        // Nếu người dùng chưa đăng nhập => điều hướng ra trang đăng nhập
        DoiTuongSuDung customer = (DoiTuongSuDung) session.getAttribute("user");
        if (customer == null) {
            model.addAttribute("taiKhoan", new TKDoiTuongSuDung());
            return "LoginPage";
        }

        // Lấy thông tin phim
        Phim phim = bookingService.findPhimById(idPhim);
        session.setAttribute("phim", phim);

        // Nếu người dùng chưa chọn ngày chiếu => lấy ngày hôm nay
        if (ngayChieu == null) {
            ngayChieu = LocalDate.now();
        }

        // Lấy danh sách hình thức chiếu
        List<HinhThucChieu> dsHinhThucChieu = Arrays.asList(HinhThucChieu.values());

        // Ban đầu người dùng chưa chọn hình thức chiếu -> lấy hình thức chiếu đầu tiên
        if (hinhThucChieu == null && !dsHinhThucChieu.isEmpty()) {
            hinhThucChieu = dsHinhThucChieu.getFirst();
        }

        // Lấy danh sách suất chiếu của từng rạp phim lọc theo phim ngày chiếu và hình thức chiếu
        Map<String, List<SuatChieu>> groupedByRap = bookingService.getShowtimesForCinema(ngayChieu, hinhThucChieu, phim);

        model.addAttribute("ngayChieu", ngayChieu);
        model.addAttribute("hinhThucChieu", hinhThucChieu);
        model.addAttribute("danhSachHinhThuc", dsHinhThucChieu);
        model.addAttribute("groupedByRap", groupedByRap);
        return "ShowtimePage";
    }

    // Hiển thị sơ đồ ghế
    @GetMapping("/so-do-ghe")
    public String hienThiSoDoGhe(@RequestParam ("idSuatChieu") int idSuatChieu,
                                 Model model, HttpSession session) {
        // Lấy thông tin suất chiếu
        SuatChieu suatChieu = bookingService.findById(idSuatChieu);
        session.setAttribute("suatChieu", suatChieu);

        // Lấy danh sách ghế đã được đặt của suất chiếu
        Set<Ghe> danhSachGheDaDat = suatChieu.getDanhSachGheDaDat();

        // Lấy danh sách dãy ghế của phòng chiếu phim
        List<DayGhe> danhSachDayGhe = bookingService.findAllDayGhe(suatChieu.getPhongChieuPhim());

        List<String> danhSachGheDuocChonIds = new ArrayList<>();
        
        model.addAttribute("danhSachGheDuocChonIds",danhSachGheDuocChonIds);
        model.addAttribute("danhSachDayGhe", danhSachDayGhe);
        model.addAttribute("danhSachGheDaDat", danhSachGheDaDat);
        return "SeatingPlanPage";
    }

    // Hiển thị combo bắp nước
    @GetMapping("/combo-list")
    public String showComboPage(@RequestParam("danhSachGheDuocChon")
                                String danhSachGheDuocChon,
                                @RequestParam("danhSachGheDuocChonIds")
                                String danhSachGheDuocChonIds,
                                @RequestParam("tongGiaVe") int tongGiaVe,
                                HttpSession session,
                                Model model) {
        // Lấy danh sách combo
        List<ComboBapNuoc> comboList = bookingService.findAllComboBapNuoc();

        // Thực hiện lưu danh sách ghế người dùng chọn vô session
        session.setAttribute("danhSachGheDuocChon",danhSachGheDuocChon);
        session.setAttribute("danhSachGheDuocChonIds", danhSachGheDuocChonIds);

        model.addAttribute("tongGiaVe", tongGiaVe);
        model.addAttribute("danhSachCombo", comboList);

        return "ChoosePopcornDrinkComboPage";
    }

    @PostMapping("/thanh-toan")
    public String hienTrangThanhToan(@RequestParam("tongVePrice") int tongVePrice,
                                     @RequestParam("tongComboVaVe") int tongComboVaVe,
                                     @RequestParam("giaTienCombo") int giaTienCombo,
                                     @RequestParam Map<String, String> tatCaThamSo,
                                     Model model, HttpSession session) {
        // Tạo combo số lượng được người dùng chọn
        Map<Integer, Integer> comboSoLuong = bookingService.extractComboSoLuong(tatCaThamSo);

        // Lưu lại thông tin để hiện lại trên UI khi áp dụng mã giảm giá hoặc reload page
        session.setAttribute("tongVePrice", tongVePrice);
        session.setAttribute("tongComboVaVe", tongComboVaVe);
        session.setAttribute("giaTienCombo", giaTienCombo);
        session.setAttribute("danhSachComboDuocChon",comboSoLuong);
        session.setAttribute("tongTienSauGiam", (double) tongComboVaVe);

        // Lấy đối tượng khách hàng
        int idcustomer = (int) session.getAttribute("idCustomer");
        DoiTuongSuDung customer = bookingService.findDoiTuongSuDungById(idcustomer);
        model.addAttribute("doiTuongSuDung", customer);
        return "PaymentPage";
    }

    @PostMapping("/ap-dung-ma-giam-gia")
    public String apDungMaGiamGia(@ModelAttribute("maGiamGia") MaGiamGia maGiamGia,
                                  @RequestParam("tongComboVaVe") String tongTien,
                                  Model model,
                                  HttpSession session) {
        // Lưu mã giảm giá vào session để cập nhật trạng thái mã giảm giá khi tiến hành thanh toán
        session.setAttribute("maGiamGiaDaChon", maGiamGia);

        // Tổng hoá đơn chưa giảm
        double tongHoaDon = Double.parseDouble(tongTien);
        double tienDuocGiam;
        double tongSauGiam;

        if (bookingService.checkMaGiamGia(tongHoaDon, maGiamGia)) {
            model.addAttribute("error", "Mã giảm giá không hợp lệ");
        }
        else {
            // Áp dụng giảm giá hợp lệ
            tienDuocGiam = bookingService.tinhTienGiam(tongHoaDon, maGiamGia);

            tongSauGiam = tongHoaDon - tienDuocGiam;

            session.setAttribute("tongTienSauGiam", tongSauGiam);
            session.setAttribute("soTienGiam", tienDuocGiam);
        }

        int idcustomer = (int) session.getAttribute("idCustomer");
        // Lấy đối tượng sử dụng
        DoiTuongSuDung doiTuongSuDung = bookingService.findDoiTuongSuDungById(idcustomer);
        model.addAttribute("doiTuongSuDung", doiTuongSuDung);

        return "PaymentPage";
    }

    @PostMapping("/dat-lai-ma-giam-gia")
    public String datLaiMaGiamGia(HttpSession session, Model model) {
        // Xoá các dữ liệu liên quan
        session.removeAttribute("tongHoaDonSauGiam");
        session.removeAttribute("soTienGiam");
        session.removeAttribute("maGiamDaChon");

        // Lấy dữ liệu từ session
        Integer tongComboVaVe = (Integer) session.getAttribute("tongComboVaVe");
        int idcustomer = (int) session.getAttribute("idCustomer");
        DoiTuongSuDung doiTuongSuDung = bookingService.findDoiTuongSuDungById(idcustomer);
        session.setAttribute("doiTuongSuDung",doiTuongSuDung);

        // Đặt lại tổng tiền sau khi giảm và số tiền giảm giá
        double tongTienSauGiam = tongComboVaVe;
        double soTienGiam = 0.0;
        session.setAttribute("tongTienSauGiam", tongTienSauGiam);
        session.setAttribute("soTienGiam", soTienGiam);

        // Lưu đối tượng để lấy danh sách mã giảm giá
        model.addAttribute("doiTuongSuDung", doiTuongSuDung);
        return "PaymentPage";
    }
}