package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.DoiTuongSuDungService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class ProfileController {
    private final DoiTuongSuDungService doiTuongSuDungService;

    @Autowired
    public ProfileController(DoiTuongSuDungService profileService) {
        this.doiTuongSuDungService = profileService;
    }

    @GetMapping("/profile")
    public String showMyProfile(Model model, HttpSession session) {
        int idCustomer = (int) session.getAttribute("idCustomer");
        DoiTuongSuDung doiTuongSuDung = doiTuongSuDungService.getDoiTuongSuDungById(idCustomer);
        model.addAttribute("khachHang", doiTuongSuDung);
        return "ProfilePage";
    }

    @PostMapping("/profile/update")
    public String upDateMyProfile(Model model, HttpSession session, @ModelAttribute DoiTuongSuDung doiTuongSuDung) {
        return "ProfilePage";
    }
}
