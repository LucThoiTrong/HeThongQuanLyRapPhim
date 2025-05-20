package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.*;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/booking")
public class BookingController {
    private final BookingService bookingService;
    private final UserService doiTuongSuDungService;

    @Autowired
    public BookingController(BookingService bookingService,
                             UserService doiTuongSuDungService) {
        this.bookingService = bookingService;
        this.doiTuongSuDungService = doiTuongSuDungService;
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
        Map<String, List<SuatChieu>> groupedByRap = bookingService.getShowtimesForCinema(ngayChieu, hinhThucChieu);

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

        // Lấy danh sách ghế đã được đặt
        Set<Ghe> danhSachGheDaDat = suatChieu.getDanhSachGheDaDat();

        // Lấy danh sách dãy ghế của phòng chiếu phim
        List<DayGhe> danhSachDayGhe = bookingService.findAllDayGhe(suatChieu.getPhongChieuPhim());

        model.addAttribute("danhSachDayGhe", danhSachDayGhe);
        model.addAttribute("danhSachGheDaDat", danhSachGheDaDat);
        return "SeatingPlanPage";
    }

    // Hiển thị combo bắp nước
    @GetMapping("/combo-list")
    public String showComboPage(@RequestParam("danhSachGheDuocChon") String danhSachGheDuocChon,
                                @RequestParam("tongGiaVe") int tongGiaVe,
                                HttpSession session,
                                Model model) {
        List<ComboBapNuoc> comboList = bookingService.findAllComboBapNuoc();
        // ban dau phai dat soTienduocgiam va tonghoadonsau giam
        // la 0.0 de khong bi lay tu session truoc
        session.setAttribute("soTienGiam", 0.0);
        session.setAttribute("tongHoaDonSauGiam", 0.0);
        session.setAttribute("danhSachGheDuocChon",danhSachGheDuocChon);

        model.addAttribute("tongGiaVe", tongGiaVe);
        model.addAttribute("danhSachCombo", comboList);
        return "ChoosePopcornDrinkComboPage";
    }

    @PostMapping("/thanh-toan")
    public String hienTrangThanhToan(@RequestParam("danhSachGheDuocChon") String danhSachGheDuocChon,
                                     @RequestParam("tongVePrice") int tongVePrice,
                                     @RequestParam("tongComboVaVe") int tongComboVaVe,
                                     @RequestParam("giaTienCombo") int giaTienCombo,
                                     @RequestParam Map<String, String> tatCaThamSo,
                                     Model model, HttpSession session) {
        //cai nay de khi load lai khong bi loi
        double tongTienSauGiam = tongComboVaVe;
        double soTienGiam = Optional.ofNullable((Double) session.getAttribute("soTienGiam")).orElse(0.0);
        //lay danh sach combo duoc chon
        Map<Integer, Integer> comboSoLuong = new HashMap<>();

        for (Map.Entry<String, String> entry : tatCaThamSo.entrySet()) {
            String key = entry.getKey();
            // Kiểm tra xem key có phải là số (ID của ComboBapNuoc) hay không
            if (key.matches("\\d+")) { // Chỉ lấy key là số
                try {
                    int comboId = Integer.parseInt(key);
                    int soLuong = Integer.parseInt(entry.getValue());
                    if (soLuong > 0) { // Chỉ thêm nếu số lượng > 0
                        comboSoLuong.put(comboId, soLuong);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi định dạng số cho combo ID: " + key);
                }
            }
        }
        // luu thong tin ghe duoc chon, tienVe, tienCombo, tienTongHoaDonChuaGiamGia/DaGiamGia
        // vao session de reload lai trang
        // khi ap dung ma giam gia
        session.setAttribute("danhSachGheDuocChon", danhSachGheDuocChon);
        session.setAttribute("tongVePrice", tongVePrice);
        session.setAttribute("tongComboVaVe", tongComboVaVe);
        session.setAttribute("giaTienCombo", giaTienCombo);
        session.setAttribute("danhSachComboDuocChon",comboSoLuong);
        session.setAttribute("tongTienSauGiam",tongTienSauGiam);

        //lay đối tượng
        int idcustomer = (int) session.getAttribute("idCustomer");
        DoiTuongSuDung customer = doiTuongSuDungService.getDoiTuongSuDungById(idcustomer);
        session.setAttribute("doiTuongSuDung",customer);
        model.addAttribute("doiTuongSuDung", customer);
        return "PaymentPage";
    }

    @PostMapping("/ap-dung-ma-giam-gia")
    public String apDungMaGiamGia(@ModelAttribute("maGiamGia") MaGiamGia maGiamGia,
                                  @RequestParam("tongComboVaVe") String tongTien,
                                  Model model,
                                  HttpSession session) {
        //luu ma giam gia vao session de cap nhat trang thai ma giam gia khi payment
        session.setAttribute("maGiamGiaDaChon", maGiamGia);
        //tong hoa don ban dau chua giam
        double tongHoaDon = Double.parseDouble(tongTien);
        double tienDuocGiam = 0.0;
        double tongSauGiam = 0.0;

        LocalDateTime now = LocalDateTime.now();
        if (maGiamGia.getNgayBatDauApDung().isAfter(now)) {
            model.addAttribute("error", "Mã giảm giá chưa đến hạn để sử dụng!");
        } else if (tongHoaDon < maGiamGia.getHanMucApDung()) {
            model.addAttribute("error", "Chưa đủ điều kiện để sử dụng mã giảm giá!");
        } else {
            // Áp dụng giảm giá hợp lệ
            tienDuocGiam = tongHoaDon * maGiamGia.getPhanTramGiamGia() / 100;
            if (tienDuocGiam > maGiamGia.getGiaTriGiamToiDa()) {
                tienDuocGiam = maGiamGia.getGiaTriGiamToiDa();
            }
            tongSauGiam = tongHoaDon - tienDuocGiam;

            session.setAttribute("tongTienSauGiam", tongSauGiam);
            session.setAttribute("soTienGiam", tienDuocGiam);
        }
        int idcustomer = (int) session.getAttribute("idCustomer");
        // Lấy đối tượng sử dụng
        DoiTuongSuDung doiTuongSuDung = doiTuongSuDungService.getDoiTuongSuDungById(idcustomer);
        model.addAttribute("doiTuongSuDung", doiTuongSuDung);
        return "PaymentPage";
    }

    @PostMapping("/dat-lai-ma-giam-gia")
    public String datLaiMaGiamGia(HttpSession session, Model model) {
        // xoa cac gia tri lien quan
        session.removeAttribute("tongHoaDonSauGiam");
        session.removeAttribute("soTienGiam");
        session.removeAttribute("maGiamDaChon");

        // Lấy dữ liệu từ session
        Integer tongComboVaVe = (Integer) session.getAttribute("tongComboVaVe");
        int idcustomer = (int) session.getAttribute("idCustomer");
        DoiTuongSuDung doiTuongSuDung = doiTuongSuDungService.getDoiTuongSuDungById(idcustomer);
        session.setAttribute("doiTuongSuDung",doiTuongSuDung);

        //dat lai tong tien sau khi giam va so tien giam gia
        double tongTienSauGiam = tongComboVaVe;
        double soTienGiam = 0.0;
        session.setAttribute("tongTienSauGiam", tongTienSauGiam);
        session.setAttribute("soTienGiam", soTienGiam);
        //de lay danh sach ma giam gia
        model.addAttribute("doiTuongSuDung", doiTuongSuDung);
        return "PaymentPage";
    }

    //neu het thoi gian giu ghe -> chuyen ve trang chu
    @GetMapping("/return-view")
    public String returnView(HttpSession session,Model model) {
        //xoa het session va tro lai trang dang nhap -> dang nhap lai
        session.invalidate();
        model.addAttribute("taiKhoan", new TKDoiTuongSuDung());
        return "LoginPage";
    }
}
