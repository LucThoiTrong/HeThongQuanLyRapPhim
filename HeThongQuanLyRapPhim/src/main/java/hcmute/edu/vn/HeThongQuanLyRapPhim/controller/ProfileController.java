package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TKDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.DoiTuongSuDungService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.TKDoiTuongService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class ProfileController {
    private final DoiTuongSuDungService doiTuongSuDungService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TKDoiTuongService tkDoiTuongService;
    @Autowired
    public ProfileController(DoiTuongSuDungService profileService, BCryptPasswordEncoder passwordEncoder, TKDoiTuongService tkDoiTuongService) {
        this.doiTuongSuDungService = profileService;
        this.passwordEncoder = passwordEncoder;
        this.tkDoiTuongService = tkDoiTuongService;
    }

    @GetMapping("/profile")
    public String showMyProfile(Model model, HttpSession session) {
        int idCustomer = (int) session.getAttribute("idCustomer");
        DoiTuongSuDung doiTuongSuDung = doiTuongSuDungService.getDoiTuongSuDungById(idCustomer);
        model.addAttribute("khachHang", doiTuongSuDung);
        return "ProfilePage";
    }

    @PostMapping("/profile/update")
    public String upDateMyProfile(HttpSession session, @ModelAttribute DoiTuongSuDung doiTuongSuDung, RedirectAttributes redirectAttributes) {
        int idCustomer = (int) session.getAttribute("idCustomer");
        DoiTuongSuDung customer = doiTuongSuDungService.getDoiTuongSuDungById(idCustomer);
        if(customer != null) {
            customer.setHoTen(doiTuongSuDung.getHoTen());
            customer.setEmail(doiTuongSuDung.getEmail());
            customer.setSoDienThoai(doiTuongSuDung.getSoDienThoai());
            customer.setNgaySinh(doiTuongSuDung.getNgaySinh());
            customer.setGioiTinh(doiTuongSuDung.getGioiTinh());

            DoiTuongSuDung result = doiTuongSuDungService.updateDoiTuongSuDung(customer);
            if(result != null) {
                redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công");
            } else {
                redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin không thành công");
            }
        }
        return "redirect:/user/profile";
    }

    @PostMapping("/profile/change-password")
    public String upDateMyPassword(HttpSession session, @RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword, RedirectAttributes redirectAttributes) {
        int idCustomer = (int) session.getAttribute("idCustomer");
        DoiTuongSuDung customer = doiTuongSuDungService.getDoiTuongSuDungById(idCustomer);
        if(customer != null) {
            TKDoiTuongSuDung tkDoiTuongSuDung = customer.getTkDoiTuongSuDung();
            if (passwordEncoder.matches(currentPassword, tkDoiTuongSuDung.getMatKhau())) {
                tkDoiTuongSuDung.setMatKhau(passwordEncoder.encode(newPassword));
                TKDoiTuongSuDung result = tkDoiTuongService.updatePassword(tkDoiTuongSuDung);
                if(result != null) {
                    redirectAttributes.addFlashAttribute("message", "Cập nhật mật khẩu thành công");
                } else {
                    redirectAttributes.addFlashAttribute("message", "Cập nhật mật khẩu thất bại");
                }
            } else {
                redirectAttributes.addFlashAttribute("message", "Bạn nhập mật khẩu hiện tại không đúng vui lòng kiểm tra lại");
            }
        }
        return "redirect:/user/profile";
    }
}