package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.*;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/revenue")
public class RevenueController {
    private final RevenueService revenueService;

    @Autowired
    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping("/")
    public String showRapPhimPage(Model model) {
        model.addAttribute("dsRapPhim", revenueService.getAllRapPhim());
        return "RevenueCinemaOverviewPage";
    }

    // Show tổng doanh thu 12 tháng
    @GetMapping("/{idRapPhim}/{nam}")
    public String showRevenuePage(Model model,@PathVariable int nam, @PathVariable int idRapPhim) {
        RapPhim rapPhim = revenueService.getRapPhimById(idRapPhim);
        Map<String, Double> tongDoanhThu = revenueService.getMonthlyRevenueByYearAndCinema(nam, rapPhim);

        model.addAttribute("rapPhim", rapPhim);
        model.addAttribute("nam", nam);
        model.addAttribute("tongDoanhThu", tongDoanhThu);

        return "RevenueCinemaPage";
    }

    @GetMapping("/{idRapPhim}/{nam}/{thang}")
    public String showRevenueDetailPage(Model model, @PathVariable int idRapPhim, @PathVariable int nam, @PathVariable int thang) {
        RapPhim rapPhim = revenueService.getRapPhimById(idRapPhim);

        Map<String, Double> tongDoanhThuTungPhim = revenueService.getMovieRevenueByMonthYearAndCinema(nam, thang, rapPhim);

        Map<String, Integer> soLuongMuaTungCombo = revenueService.getComboQuantityByMonthYearAndCinema(nam, thang, rapPhim);

        model.addAttribute("tongDoanhThuTungPhim", tongDoanhThuTungPhim);
        model.addAttribute("soLuongMuaTungCombo", soLuongMuaTungCombo);
        model.addAttribute("rapPhim", rapPhim);
        model.addAttribute("nam", nam);
        model.addAttribute("selectedMonth", thang);

        return "RevenueCinemaDetailPage";
    }
}