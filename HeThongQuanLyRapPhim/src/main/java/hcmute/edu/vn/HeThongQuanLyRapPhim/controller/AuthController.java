package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/author")
public class AuthController {
    @PostMapping("/author/password")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, Model model) {
        
        return "";
    }

}
