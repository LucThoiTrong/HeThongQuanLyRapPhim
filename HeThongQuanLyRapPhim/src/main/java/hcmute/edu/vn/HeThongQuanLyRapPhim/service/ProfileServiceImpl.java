package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    // Xem thông tin cá nhân
    @Override
    public DoiTuongSuDung getDoiTuongSuDung(Integer id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    }

    // Cập nhật thông tin cá nhân
    @Override
    public DoiTuongSuDung updateProfile(DoiTuongSuDung newProfile) {
        return profileRepository.save(newProfile);
    }
}