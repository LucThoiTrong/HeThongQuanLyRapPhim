package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.GioiTinh;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.LoaiDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("error", "");
        model.addAttribute("username", "");
        model.addAttribute("password", "");
        return "Login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("tenDangNhap") String tenDangNhap,
                        @RequestParam("matKhau") String matKhau,
                        Model model) {
        String result = authService.login(tenDangNhap, matKhau);
        model.addAttribute("tenDangNhap", tenDangNhap);
        model.addAttribute("matKhau", matKhau);
        if (result.equals("success")) {
            return "redirect:/home";
        } else {
            model.addAttribute("message", result);
            model.addAttribute("success", false);
            return "Login";
        }
    }


    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("doiTuongSuDung", new DoiTuongSuDung());
        model.addAttribute("gioiTinhList", GioiTinh.values());
        model.addAttribute("error", "");
        return "Register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("doiTuongSuDung") DoiTuongSuDung doiTuongSuDung,
            BindingResult result,
            @RequestParam String tenDangNhap,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("gioiTinhList", GioiTinh.values());
            model.addAttribute("error", "");
            return "Register";
        }

        try {
            doiTuongSuDung.setLoaiDoiTuongSuDung(LoaiDoiTuongSuDung.KHACH_HANG);
            authService.register(doiTuongSuDung, tenDangNhap, password, confirmPassword);
            model.addAttribute("message", "Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản.");
            return "Login";
        } catch (Exception e) {
            model.addAttribute("gioiTinhList", GioiTinh.values());
            model.addAttribute("error", e.getMessage());
            return "Register";
        }
    }

    @GetMapping("/auth/verify")
    public String verifyAccount(@RequestParam("id") int id, Model model) {
        try {
            authService.verifyAccount(id);
            model.addAttribute("message", "Tài khoản đã được xác thực! Vui lòng đăng nhập.");
            return "Login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Login";
        }
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("error", "");
        return "Change-Password";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam String username,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmNewPassword,
            Model model) {
        try {
            authService.changePassword(username, oldPassword, newPassword, confirmNewPassword);
            model.addAttribute("message", "Đổi mật khẩu thành công! Vui lòng đăng nhập lại.");
            return "Login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Change-Password";
        }
    }
}