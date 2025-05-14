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

    @GetMapping("/signin")
    public String showLoginForm(Model model, @ModelAttribute("taiKhoan") TKDoiTuongSuDung taiKhoan) {
        model.addAttribute("taiKhoan", taiKhoan!=null ? taiKhoan:new TKDoiTuongSuDung());
        return "Login";
    }

    @PostMapping("/signin")
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
            return "redirect:/signin";
        }
    }

    @GetMapping("/signout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
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
            TKDoiTuongSuDung savedTK =  authService.register(doiTuongSuDung, taiKhoan.getTenDangNhap(), taiKhoan.getMatKhau());
            emailService.sendVerificationEmail(doiTuongSuDung.getEmail(), savedTK.getIdTKDoiTuongSuDung());
            redirectAttributes.addFlashAttribute("message", "Đăng ký tài khoản thành công vui lòng kiểm tra email để kích hoạt tài khoản");
            redirectAttributes.addFlashAttribute("message_type", "SUCCESS");
            // Điều hướng qua 1 UI khác thông báo
            return "redirect:/signin";
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
            return "redirect:/signin";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/signin";
        }
    }
    //nhan vao la ra trang nhap dia chi email
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "ForgotPasswordPage";
    }

    @PostMapping("/submit-email")
    public String submitEmail(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        DoiTuongSuDung doiTuongSuDung = authService.getDoiTuongSuDungByEmail(email);
        if (doiTuongSuDung == null || doiTuongSuDung.getTkDoiTuongSuDung() == null) {
            redirectAttributes.addFlashAttribute("message", "Email không tồn tại!");
            redirectAttributes.addFlashAttribute("message_type", "ERROR");
            return "redirect:/forgot-password";
        }

        try {
            TKDoiTuongSuDung tk = doiTuongSuDung.getTkDoiTuongSuDung();
            emailService.sendResetPasswordEmail(email, tk.getIdTKDoiTuongSuDung());
            redirectAttributes.addFlashAttribute("message", "Email khôi phục mật khẩu đã được gửi!");
            redirectAttributes.addFlashAttribute("message_type", "SUCCESS");
            return "NotificationForgotPassword"; // Chuyển hướng về trang thông báo
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Lỗi khi gửi email: " + e.getMessage());
            redirectAttributes.addFlashAttribute("message_type", "ERROR");
            return "redirect:/forgot-password";
        }
    }
    //de show form
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("id") int id, Model model) {
        model.addAttribute("id", id); // Truyền id cho form
        return "ResetPasswordPage";
    }

    //submit pass moi
    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam("newpassword") String newPassword,
            @RequestParam("id") int id) {
        authService.resetPassword(id, newPassword);
        return "redirect:/signin";
    }
}