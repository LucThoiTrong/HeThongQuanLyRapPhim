package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class ManageController {

    @Autowired
    private CinemaService cinemaService;

    // Hiển thị trang quản lý chính
    @GetMapping("")
    public String showManagementPage() {
        return "page-manager";
    }

    // Hiển thị danh sách dãy ghế
    @GetMapping("/cinema/list")
    public String showList(Model model) {
        model.addAttribute("dsDayGhe", cinemaService.getAllCinemas());
        return "list-cinema";
    }
}
