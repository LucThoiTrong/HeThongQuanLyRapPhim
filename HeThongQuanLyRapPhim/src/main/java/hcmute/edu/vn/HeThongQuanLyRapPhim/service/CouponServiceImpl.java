package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.DiscountRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {
    private final DiscountRepository discountRepository;
    private final UserRepository userRepository;

    @Autowired
    public CouponServiceImpl(DiscountRepository discountRepository, UserRepository userRepository) {
        this.discountRepository = discountRepository;
        this.userRepository = userRepository;
    }

    // Lấy danh sách mã giảm hiện có nhưng chưa bị thu thập
    public List<MaGiamGia> findAllMaGiamGia() {
        return discountRepository.findAllByDoiTuongSuDungIsNull();
    }

    // Thu thập mã giảm giá
    public MaGiamGia getMaGiamGia(int idMaGiamGia, int idKhachHang) {
        DoiTuongSuDung customer = userRepository.findById(idKhachHang).orElse(null);

        MaGiamGia maGiamGia = discountRepository.findById(idMaGiamGia).orElse(null);

        if( maGiamGia != null) {
            maGiamGia.setDoiTuongSuDung(customer);
            return discountRepository.save(maGiamGia);
        }
        return null;
    }
}
