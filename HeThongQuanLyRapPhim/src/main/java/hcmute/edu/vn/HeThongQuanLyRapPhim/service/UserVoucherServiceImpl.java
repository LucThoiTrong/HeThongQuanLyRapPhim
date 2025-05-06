package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.UserVoucherRepository;

import java.util.Set;

public class UserVoucherServiceImpl implements UserVoucherService {
    private final UserVoucherRepository userVoucherRepository;

    public UserVoucherServiceImpl(UserVoucherRepository userVoucherRepository) {
        this.userVoucherRepository = userVoucherRepository;
    }


    @Override
    public Set<MaGiamGia> getAllVoucher(DoiTuongSuDung doiTuongSuDung) {
        return userVoucherRepository.findAllByDoiTuongSuDung(doiTuongSuDung);
    }

    @Override
    public void addVoucher(DoiTuongSuDung doiTuongSuDung, MaGiamGia maGiamGia) {
        maGiamGia.setDoiTuongSuDung(doiTuongSuDung);
        userVoucherRepository.save(maGiamGia);
    }
}
