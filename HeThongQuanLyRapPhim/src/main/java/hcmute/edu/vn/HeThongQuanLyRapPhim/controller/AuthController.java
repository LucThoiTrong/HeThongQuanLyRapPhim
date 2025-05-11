package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.LoaiDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TKDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.AuthService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;

    @Autowired
    public AuthController(AuthService authService, EmailService emailService) {
        this.authService = authService;
        this.emailService = emailService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model, @ModelAttribute("taiKhoan") TKDoiTuongSuDung taiKhoan) {
        model.addAttribute("taiKhoan", taiKhoan!=null ? taiKhoan:new TKDoiTuongSuDung());
        return "Login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("taiKhoan") TKDoiTuongSuDung taiKhoan,
                        Model model, RedirectAttributes redirectAttributes,
                        HttpSession session) {
        DoiTuongSuDung result = authService.login(taiKhoan.getTenDangNhap(), taiKhoan.getMatKhau());
        model.addAttribute("tenDangNhap", taiKhoan.getTenDangNhap());
        model.addAttribute("matKhau", taiKhoan.getMatKhau());
        if (result != null) {
            redirectAttributes.addFlashAttribute("message", "Đăng nhập thành công");
            redirectAttributes.addFlashAttribute("message_type", "SUCCESS");
            if (result.getLoaiDoiTuongSuDung() == LoaiDoiTuongSuDung.KHACH_HANG) {
                session.setAttribute("idCustomer", result.getIdDoiTuongSuDung());
                session.setAttribute("user", result);
                return "redirect:/";
            } else {
                return "redirect:/manage/";
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "Đăng nhập thất bại!, vui lòng kiểm tra lại tên đăng nhập và mật khẩu");
            redirectAttributes.addFlashAttribute("message_type", "ERROR");
            redirectAttributes.addFlashAttribute("taiKhoan", taiKhoan);
            return "redirect:/login";
        }
    }


    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        if (!model.containsAttribute("doiTuongSuDung")) {
            model.addAttribute("doiTuongSuDung", new DoiTuongSuDung());
        }
        if (!model.containsAttribute("taiKhoan")) {
            model.addAttribute("taiKhoan", new TKDoiTuongSuDung());
        }
        return "Register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute("doiTuongSuDung") DoiTuongSuDung doiTuongSuDung,
            @ModelAttribute("taiKhoan") TKDoiTuongSuDung taiKhoan,
            RedirectAttributes redirectAttributes) {

        doiTuongSuDung.setLoaiDoiTuongSuDung(LoaiDoiTuongSuDung.KHACH_HANG);
        try {
            authService.register(doiTuongSuDung, taiKhoan.getTenDangNhap(), taiKhoan.getMatKhau());
            emailService.sendVerificationEmail(doiTuongSuDung.getEmail(),doiTuongSuDung.getIdDoiTuongSuDung());
            redirectAttributes.addFlashAttribute("message", "Đăng ký tài khoản thành công vui lòng kiểm tra email để kích hoạt tài khoản");
            redirectAttributes.addFlashAttribute("message_type", "SUCCESS");
            // Điều hướng qua 1 UI khác thông báo
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("signupErrorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("taiKhoan", taiKhoan);
            redirectAttributes.addFlashAttribute("doiTuongSuDung", doiTuongSuDung);
            return "redirect:/register";
        }
    }


    @GetMapping("/auth/verify")
    public String verifyAccount(@RequestParam("id") int id, Model model) {
        try {
            authService.verifyAccount(id);
            model.addAttribute("message", "Tài khoản đã được xác thực! Vui lòng đăng nhập.");
            return "redirect:/login";
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