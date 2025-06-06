package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.CinemaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manage")
public class ManageController {
    private final CinemaService cinemaService;

    @Autowired
    public ManageController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    // Hiển thị trang quản lý chính
    @GetMapping("/")
    public String showManagementPage(Model model, HttpSession session) {
        DoiTuongSuDung admin = (DoiTuongSuDung) session.getAttribute("admin");
        if(admin == null) {
            return "redirect:/signin";
        }
        model.addAttribute("dsRapPhim", cinemaService.getAllCinemas());
        return "ManagePage";
    }
}