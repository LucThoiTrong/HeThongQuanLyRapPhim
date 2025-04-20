package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // Lấy thông tin cá nhân
    @GetMapping("/account")
    private DoiTuongSuDung getMyProfile(@RequestParam Integer id) {
        return profileService.getDoiTuongSuDung(id);
    }

    // Cập nhật thông tin cá nhân
    @PutMapping("/account")
    private DoiTuongSuDung updateMyProfile(@RequestBody DoiTuongSuDung profile) {
        return profileService.updateProfile(profile);
    }
}
