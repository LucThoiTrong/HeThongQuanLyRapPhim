package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TKDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    private final UserAccountRepository tkDoiTuongSuDungRepository;

    @Autowired
    public UserAccountServiceImpl(UserAccountRepository tkDoiTuongSuDungRepository) {
        this.tkDoiTuongSuDungRepository = tkDoiTuongSuDungRepository;
    }

    @Override
    public TKDoiTuongSuDung updatePassword(TKDoiTuongSuDung tkDoiTuongSuDung) {
        return tkDoiTuongSuDungRepository.save(tkDoiTuongSuDung);
    }
}
