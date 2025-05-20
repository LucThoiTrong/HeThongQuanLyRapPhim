package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TKDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class ProfileController {
    private final ProfileService profileService;
    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // Lấy thông tin cá nhân
    @GetMapping("/profile")
    public String showMyProfile(Model model, HttpSession session) {
        int idCustomer = (int) session.getAttribute("idCustomer");

        DoiTuongSuDung user = profileService.getProfile(idCustomer);

        model.addAttribute("khachHang", user);
        return "ProfilePage";
    }

    // Cập nhật thông tin cá nhân
    @PostMapping("/profile/update")
    public String updateMyProfile(HttpSession session, @ModelAttribute DoiTuongSuDung doiTuongSuDung, RedirectAttributes redirectAttributes) {
        int idCustomer = (int) session.getAttribute("idCustomer");

        DoiTuongSuDung result = profileService.updateProfile(doiTuongSuDung, idCustomer);
        if (result != null) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin không thành công");
        }
        return "redirect:/user/profile";
    }

    // Cập nhật mật khẩu
    @PostMapping("/profile/change-password")
    public String updateMyPassword(HttpSession session, @RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword, RedirectAttributes redirectAttributes) {
        int idCustomer = (int) session.getAttribute("idCustomer");
        // Check mật khẩu cũ có khớp hay không
        if(profileService.checkPassword(currentPassword, idCustomer)) {
            TKDoiTuongSuDung result = profileService.updatePassword(newPassword, idCustomer);
            if(result != null) {
                redirectAttributes.addFlashAttribute("message", "Cập nhật mật khẩu thành công");
            } else {
                redirectAttributes.addFlashAttribute("message", "Cập nhật mật khẩu thất bại");
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "Bạn nhập mật khẩu hiện tại không đúng vui lòng kiểm tra lại");
        }
        return "redirect:/user/profile";
    }
}