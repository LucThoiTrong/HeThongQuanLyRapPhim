package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import java.util.Set;

public interface UserVoucherService {
    Set<MaGiamGia> getAllVoucher(DoiTuongSuDung idDoiTuongSuDung);
    void addVoucher(DoiTuongSuDung doiTuongSuDung, MaGiamGia maGiamGia);
}
