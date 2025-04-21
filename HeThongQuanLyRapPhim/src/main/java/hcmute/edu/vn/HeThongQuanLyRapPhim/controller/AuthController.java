package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/author")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/author/password")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, Model model) {
        
        return "";
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, Object> request) {
        try {
            String hoTen = (String) request.get("hoTen");
            String email = (String) request.get("email");
            String soDienThoai = (String) request.get("soDienThoai");
            String ngaySinhStr = (String) request.get("ngaySinh");
            String tenDangNhap = (String) request.get("tenDangNhap");
            String matKhau = (String) request.get("matKhau");

            // Validate required fields
            if (hoTen == null || email == null || soDienThoai == null || ngaySinhStr == null ||
                    tenDangNhap == null || matKhau == null) {
                throw new IllegalArgumentException("Tất cả các trường là bắt buộc!");
            }

            // Parse ngaySinh from string to LocalDateTime
            LocalDateTime ngaySinh = LocalDateTime.parse(ngaySinhStr);

            String result = authService.register(hoTen, email, soDienThoai, ngaySinh, tenDangNhap, matKhau);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token, @RequestParam("id") int id) {
        try {
            String result = authService.verifyAccount(token, id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        try {
            String tenDangNhap = request.get("tenDangNhap");
            String matKhau = request.get("matKhau");
            if (tenDangNhap == null || matKhau == null) {
                throw new IllegalArgumentException("Tên đăng nhập và mật khẩu là bắt buộc!");
            }
            String result = authService.login(tenDangNhap, matKhau);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
