package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository doiTuongSuDungRepository;

    @Autowired
    public UserServiceImpl(UserRepository doiTuongSuDungRepository) {
        this.doiTuongSuDungRepository = doiTuongSuDungRepository;
    }

    @Override
    public DoiTuongSuDung getDoiTuongSuDungById(int id) {
        return doiTuongSuDungRepository.findById(id).orElse(null);
    }
}
