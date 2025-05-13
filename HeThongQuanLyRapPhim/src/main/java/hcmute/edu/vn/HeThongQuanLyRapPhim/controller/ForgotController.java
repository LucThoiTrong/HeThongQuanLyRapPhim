package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/forgot")
public class ForgotController {
    @GetMapping("/")
    public String showForgotPage() {
        return "ForgotPasswordPage";
    }
}
