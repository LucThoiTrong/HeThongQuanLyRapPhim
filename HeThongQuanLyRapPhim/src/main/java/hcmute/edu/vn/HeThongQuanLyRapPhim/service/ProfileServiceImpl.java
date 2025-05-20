package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TKDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.UserAccountRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final UserAccountRepository userAccountRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public ProfileServiceImpl(UserRepository userRepository, UserAccountRepository userAccountRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Lấy thông tin cá nhân
    @Override
    public DoiTuongSuDung getProfile(int idUser) {
        return userRepository.findById(idUser).orElse(null);
    }

    // Cập nhật thông tin cá nhân
    @Override
    public DoiTuongSuDung updateProfile(DoiTuongSuDung newProfile, int idUser) {
        DoiTuongSuDung user = userRepository.findById(idUser).orElse(null);
        if(user != null) {
            user.setHoTen(newProfile.getHoTen());
            user.setEmail(newProfile.getEmail());
            user.setSoDienThoai(newProfile.getSoDienThoai());
            user.setNgaySinh(newProfile.getNgaySinh());
            user.setGioiTinh(newProfile.getGioiTinh());
            return userRepository.save(user);
        }
        return null;
    }

    // check mật khẩu
    @Override
    public boolean checkPassword(String password, int idUser) {
        DoiTuongSuDung user = userRepository.findById(idUser).orElse(null);
        if(user != null) {
            TKDoiTuongSuDung tkDoiTuongSuDung = user.getTkDoiTuongSuDung();
            return passwordEncoder.matches(password, tkDoiTuongSuDung.getMatKhau());
        }
        return false;
    }

    // Cập nhật mật khẩu
    @Override
    public TKDoiTuongSuDung updatePassword(String newPassword, int idUser) {
        DoiTuongSuDung user = userRepository.findById(idUser).orElse(null);
        if(user != null) {
            TKDoiTuongSuDung tkDoiTuongSuDung = user.getTkDoiTuongSuDung();
            tkDoiTuongSuDung.setMatKhau(passwordEncoder.encode(newPassword));
            return userAccountRepository.save(tkDoiTuongSuDung);
        }
        return null;
    }
}