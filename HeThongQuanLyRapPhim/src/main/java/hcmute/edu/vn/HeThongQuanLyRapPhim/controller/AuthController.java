package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.LoaiDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TKDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.AuthService;
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

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Thực hiện chức năng đăng nhập
    @GetMapping("/signin")
    public String showLoginForm(Model model, @ModelAttribute("taiKhoan") TKDoiTuongSuDung taiKhoan) {
        model.addAttribute("taiKhoan", taiKhoan != null ? taiKhoan : new TKDoiTuongSuDung());
        return "LoginPage";
    }

    @PostMapping("/signin")
    public String login(@ModelAttribute("taiKhoan") TKDoiTuongSuDung taiKhoan, RedirectAttributes redirectAttributes, HttpSession session) {
        // Check tài khoản đã được kích hoạt hay chưa
        if (!authService.checkIsAccountVerified(taiKhoan.getTenDangNhap())) {
            redirectAttributes.addFlashAttribute("message", "Đăng nhập thất bại, vui lòng kiểm tra email để kích hoạt tài khoản");
            redirectAttributes.addFlashAttribute("message_type", "ERROR");
            redirectAttributes.addFlashAttribute("taiKhoan", taiKhoan);
            return "redirect:/signin";
        }

        DoiTuongSuDung result = authService.login(taiKhoan.getTenDangNhap(), taiKhoan.getMatKhau());

        // Đăng nhập thất bại
        if (result == null) {
            redirectAttributes.addFlashAttribute("message", "Đăng nhập thất bại! Vui lòng kiểm tra lại tên đăng nhập và mật khẩu");
            redirectAttributes.addFlashAttribute("message_type", "ERROR");
            redirectAttributes.addFlashAttribute("taiKhoan", taiKhoan);
            return "redirect:/signin";
        }

        // Đăng nhập thành công
        redirectAttributes.addFlashAttribute("message", "Đăng nhập thành công");
        redirectAttributes.addFlashAttribute("message_type", "SUCCESS");

        if (result.getLoaiDoiTuongSuDung() == LoaiDoiTuongSuDung.KHACH_HANG) {
            session.setAttribute("user", result);
            session.setAttribute("idCustomer", result.getIdDoiTuongSuDung());
            return "redirect:/";
        }

        return "redirect:/manage/";
    }

    // Thực hiện chức năng đăng xuất
    @GetMapping("/signout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


    // Thực hiện chức nang đăng ký
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        if (!model.containsAttribute("doiTuongSuDung")) {
            model.addAttribute("doiTuongSuDung", new DoiTuongSuDung());
        }
        if (!model.containsAttribute("taiKhoan")) {
            model.addAttribute("taiKhoan", new TKDoiTuongSuDung());
        }
        return "RegisterPage";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute("doiTuongSuDung") DoiTuongSuDung doiTuongSuDung,
            @ModelAttribute("taiKhoan") TKDoiTuongSuDung taiKhoan,
            RedirectAttributes redirectAttributes) {
        // Kiểm tra tên đăng nhập có tồn tại hay chưa
        // Kiểm tra email đã tồn tại trong hệ thống hay chưa
        String errorMessage = null;
        if (!authService.checkUsername(taiKhoan.getTenDangNhap())) {
            errorMessage = "Tên đăng nhập đã tồn tại";
        } else if (!authService.checkEmail(doiTuongSuDung.getEmail())) {
            errorMessage = "Email đã tồn tại";
        }

        if (errorMessage != null) {
            redirectAttributes.addFlashAttribute("signupErrorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("taiKhoan", taiKhoan);
            redirectAttributes.addFlashAttribute("doiTuongSuDung", doiTuongSuDung);
            return "redirect:/register";
        }

        // Tiến hành đăng ký tài khoản
        doiTuongSuDung.setLoaiDoiTuongSuDung(LoaiDoiTuongSuDung.KHACH_HANG);
        boolean result = authService.register(doiTuongSuDung, taiKhoan.getTenDangNhap(), taiKhoan.getMatKhau());
        if (result) {
            redirectAttributes.addFlashAttribute("message", "Đăng ký tài khoản thành công vui lòng kiểm tra email để kích hoạt tài khoản");
            redirectAttributes.addFlashAttribute("message_type", "SUCCESS");
            return "redirect:/signin";
        } else {
            redirectAttributes.addFlashAttribute("signupErrorMessage", "Đăng ký tài khoản thất bại");
            redirectAttributes.addFlashAttribute("taiKhoan", taiKhoan);
            redirectAttributes.addFlashAttribute("doiTuongSuDung", doiTuongSuDung);
            return "redirect:/register";
        }
    }

    // Thực hiện xác thực tài khoản
    @GetMapping("/auth/verify")
    public String verifyAccount(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        boolean result = authService.verifyAccount(id);
        if (result) {
            redirectAttributes.addAttribute("message", "Tài khoản đã được xác thực! Vui lòng đăng nhập.");
            redirectAttributes.addFlashAttribute("message_type", "SUCCESS");
        } else {
            redirectAttributes.addFlashAttribute("signupErrorMessage", "Tài khoản xác thực thất bại");
        }
        return "redirect:/signin";
    }

    //Thực hiện chức năng nhập email - quên mật khẩu
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "ForgotPasswordPage";
    }

    @PostMapping("/submit-email")
    public String submitEmail(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        if(authService.checkEmail(email)) {
            redirectAttributes.addFlashAttribute("message", "Email không tồn tại!");
            redirectAttributes.addFlashAttribute("message_type", "ERROR");
            return "redirect:/forgot-password";
        }

        // Tiền hành gửi mail
        boolean result = authService.sendMailForgotPassword(email);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "Email khôi phục mật khẩu đã được gửi!");
            redirectAttributes.addFlashAttribute("message_type", "SUCCESS");
            return "NotificationForgotPasswordPage";
        } else {
            redirectAttributes.addFlashAttribute("message", "Lỗi khi gửi email");
            redirectAttributes.addFlashAttribute("message_type", "ERROR");
            return "redirect:/forgot-password";
        }
    }

    //Thực hiện reset password
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("id") int id, Model model) {
        model.addAttribute("id", id); // Truyền id cho form
        return "ResetPasswordPage";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam("newpassword") String newPassword,
            @RequestParam("id") int id) {
        authService.resetPassword(id, newPassword);
        return "redirect:/signin";
    }
}