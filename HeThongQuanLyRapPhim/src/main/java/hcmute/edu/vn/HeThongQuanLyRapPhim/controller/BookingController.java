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
    private final MovieService movieService;
    private final DoiTuongSuDungService doiTuongSuDungService;
    private final PopcornDrinkComboService popcornDrinkComboService;
    private final ChairService chairService;

    @Autowired
    public BookingController(BookingService bookingService, MovieService movieService,
                             DoiTuongSuDungService doiTuongSuDungService,
                             PopcornDrinkComboService popcornDrinkComboService,
                             ChairService chairService) {
        this.bookingService = bookingService;
        this.movieService = movieService;
        this.doiTuongSuDungService = doiTuongSuDungService;
        this.popcornDrinkComboService = popcornDrinkComboService;
        this.chairService = chairService;
    }
    //tim suat chieu cua phim theo ngay chieu va hinh thuc chieu
    @GetMapping("/{idPhim}")
    public String getSuatChieuTheoPhim(@PathVariable int idPhim,
                                       @RequestParam(required = false) LocalDate ngayChieu,
                                       @RequestParam(required = false) HinhThucChieu hinhThucChieu,
                                       Model model, HttpSession session) {
        //kiem tra session id nguoi dung -> neu ko co quay lai trang dang nhap
        //dang nhap lai
        DoiTuongSuDung customer = (DoiTuongSuDung) session.getAttribute("user");
        if (customer == null) {
            model.addAttribute("taiKhoan", new TKDoiTuongSuDung());
            return "Login";
        }
        else {
            // lay thong tin phim
            Phim phim = movieService.getPhimById(idPhim);
            session.setAttribute("phim", phim);

            // ban dau nguoi dung chua chon ngay chieu -> lay hien tai
            if (ngayChieu == null) {
                ngayChieu = LocalDate.now();
            }

            //lay tat ca hinh thuc chieu hien len giao dien
            List<HinhThucChieu> dsHinhThucChieu = Arrays.asList(HinhThucChieu.values());

            // ban dau nguoi dung chua chon hinh thuc chieu -> chon dau tien trong ds
            if (hinhThucChieu == null && !dsHinhThucChieu.isEmpty()) {
                hinhThucChieu = dsHinhThucChieu.getFirst();
            }

            // lay ds suat chieu loc theo phim + hinh thuc chieu
            List<SuatChieu> danhSachSuatChieu = bookingService.getSuatChieuByPhimNgayChieuAndHinhThuc(phim, ngayChieu, hinhThucChieu);

            //xu ly thong tin giao dien
            // nhom suat chieu theo RapPhim -> de hien len giao dien
            Map<String, List<SuatChieu>> groupedByRap = new LinkedHashMap<>();
            for (SuatChieu suat : danhSachSuatChieu) {
                String tenRap = suat.getPhongChieuPhim().getRapPhim().getTenRapPhim();
                groupedByRap.computeIfAbsent(tenRap, k -> new ArrayList<>()).add(suat);
            }
            // Lấy danh sách 15 ngày tiếp theo
            List<LocalDate> danhSachNgay = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                danhSachNgay.add(LocalDate.now().plusDays(i));
            }
            model.addAttribute("danhSachNgay", danhSachNgay);
            model.addAttribute("ngayChieu", ngayChieu);
            model.addAttribute("hinhThucChieu", hinhThucChieu);
            model.addAttribute("danhSachHinhThuc", dsHinhThucChieu);
            model.addAttribute("groupedByRap", groupedByRap);
            return "SuatChieu";
        }
    }
    @GetMapping("/combo-list")
    public String showComboPage(@RequestParam("danhSachGheDuocChon") String danhSachGheDuocChon,
                                @RequestParam("tongGiaVe") int tongGiaVe,
                                HttpSession session,
                                Model model) {
        List<ComboBapNuoc> comboList = popcornDrinkComboService.findAll();
        //cap nhat trang thai ghe da chon
        chairService.capNhatTrangThaiGhe(danhSachGheDuocChon,true);
        //ban dau phai dat soTienduocgiam va tonghoadonsau giam
        // la 0.0 de khong bi lay tu session truoc
        session.setAttribute("soTienGiam", 0.0);
        session.setAttribute("tongHoaDonSauGiam", 0.0);
        session.setAttribute("danhSachGheDuocChon",danhSachGheDuocChon);

        model.addAttribute("tongGiaVe", tongGiaVe);
        model.addAttribute("danhSachCombo", comboList);
        return "ChoosePopcornDrinkCombo";
    }
    @PostMapping("/thanh-toan")
    public String hienTrangThanhToan(@RequestParam("danhSachGheDuocChon") String danhSachGheDuocChon,
                                     @RequestParam("tongVePrice") int tongVePrice,
                                     @RequestParam("tongComboVaVe") int tongComboVaVe,
                                     @RequestParam("giaTienCombo") int giaTienCombo,
                                     @RequestParam Map<String, String> tatCaThamSo,
                                     Model model, HttpSession session) {
        //cai nay de khi load lai khong bi loi
        double tongTienSauGiam = (double) tongComboVaVe;
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
        //luu thong tin ghe duoc chon, tienVe, tienCombo, tienTongHoaDonChuaGiamGia/DaGiamGia
        //vao session de reload lai trang
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
        return "ThanhToan";
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
        return "ThanhToan";
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
        return "ThanhToan";
    }
    //neu het thoi gian giu ghe -> chuyen ve trang chu
    @GetMapping("/return-view")
    public String returnView(HttpSession session,Model model) {
        String danhSachGheDuocChon = (String) session.getAttribute("danhSachGheDuocChon");
        chairService.capNhatTrangThaiGhe(danhSachGheDuocChon,false);
        //xoa het session va tro lai trang dang nhap -> dang nhap lai
        session.invalidate();
        model.addAttribute("taiKhoan", new TKDoiTuongSuDung());
        return "Login.html";
    }
}
