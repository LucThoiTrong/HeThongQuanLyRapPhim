package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.*;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.CinemaService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MovieService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.PopcornDrinkComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/revenue")
public class RevenueController {
    private final CinemaService cinemaService;
    private final PopcornDrinkComboService popcornDrinkComboService;
    private final MovieService movieService;

    @Autowired
    public RevenueController(CinemaService cinemaService, PopcornDrinkComboService popcornDrinkComboService, MovieService movieService) {
        this.cinemaService = cinemaService;
        this.popcornDrinkComboService = popcornDrinkComboService;
        this.movieService = movieService;
    }

    @GetMapping("/")
    public String showRapPhimPage(Model model) {
        model.addAttribute("dsRapPhim", cinemaService.getAllCinemas());
        return "CinemaPage";
    }

    // Show tổng doanh thu 12 tháng
    @GetMapping("/{idRapPhim}/{nam}")
    public String showRevenuePage(Model model,@PathVariable int nam, @PathVariable int idRapPhim) {
        Map<String, Double> tongDoanhThu = new HashMap<>();

        // Lấy rạp phim dựa trên id
        RapPhim rapPhim = cinemaService.getCinemaById(idRapPhim);

        for(int i = 1 ; i <= 12; i++) {
            double tongTien = rapPhim.tongDoanhThuTungThang(i, nam);
            // Lưu tổng tiền tương ứng vào tháng
            tongDoanhThu.put("Tháng " + i, tongTien);
        }
        List<Integer> availableYears = new ArrayList<>();
        for (int yearValue = nam - 9; yearValue <= nam; yearValue++) {
            availableYears.add(yearValue);
        }

        double maxDoanhThuThang = 0.0;
        if (!tongDoanhThu.isEmpty()) {
            // Tìm giá trị lớn nhất trong Map tongDoanhThu
            for (Double doanhThu : tongDoanhThu.values()) {
                if (doanhThu != null && doanhThu > maxDoanhThuThang) {
                    maxDoanhThuThang = doanhThu;
                }
            }
        }

        model.addAttribute("availableYears", availableYears);
        model.addAttribute("rapPhim", rapPhim);
        model.addAttribute("nam", nam);
        model.addAttribute("tongDoanhThu", tongDoanhThu);
        model.addAttribute("maxDoanhThuThang", maxDoanhThuThang);
        return "RevenueCinemaPage";
    }

    // Show chi tiết doanh thu từng tháng
    // Đầu vào là tháng năm và từng bộ phim hoặc từng loại combo
    // Đâu ra Doanh thu từng bộ phim trong tháng và số lượng mua của từng loại combo
    @GetMapping("/{idRapPhim}/{nam}/{thang}")
    public String showRevenueDetailPage(Model model, @PathVariable int idRapPhim, @PathVariable int nam, @PathVariable int thang) {
        Map<String, Double> tongDoanhThuTungPhim = new HashMap<>();
        Map<String, Integer> soLuongMuaTungCombo = new HashMap<>();

        // Lấy rạp phim
        RapPhim rapPhim = cinemaService.getCinemaById(idRapPhim);

        // Lấy toàn bộ danh sách phim đang chiếu
        List<Phim> danhSachPhim = movieService.getMoviesByTrangThaiPhimDangChieuVaDaChieu();
        for (Phim phim : danhSachPhim) {
            double tongTien = rapPhim.tongDoanhThuTungPhim(thang, nam, phim);
            tongDoanhThuTungPhim.put(phim.getTenPhim(), tongTien);
        }

        // Lấy toàn bộ combo đang ban
        List<ComboBapNuoc> danhSachCombo = popcornDrinkComboService.findAll();
        for (ComboBapNuoc comboBapNuoc : danhSachCombo) {
            int soLuongBanRa = rapPhim.tongSoLuongTungComboBanRa(thang, nam, comboBapNuoc);
            soLuongMuaTungCombo.put(comboBapNuoc.getTenCombo(), soLuongBanRa);
        }
        model.addAttribute("tongDoanhThuTungPhim", tongDoanhThuTungPhim);
        model.addAttribute("soLuongMuaTungCombo", soLuongMuaTungCombo);
        model.addAttribute("rapPhim", rapPhim);
        model.addAttribute("nam", nam);
        model.addAttribute("selectedMonth", thang);

        return "RevenueDetail";
    }
}