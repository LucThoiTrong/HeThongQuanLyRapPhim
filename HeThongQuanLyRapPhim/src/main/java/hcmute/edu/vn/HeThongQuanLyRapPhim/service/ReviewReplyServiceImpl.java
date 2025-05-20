package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhanHoi;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.ReviewReplyRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.ReviewRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReviewReplyServiceImpl implements ReviewReplyService {
    private final ReviewReplyRepository reviewReplyRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewReplyServiceImpl(ReviewReplyRepository reviewReplyRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.reviewReplyRepository = reviewReplyRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public PhanHoi addReplyReview(PhanHoi phanHoi, int idDanhGia, int idKhachHang) {
        // Set thông tin phản hồi
        phanHoi.setThoiGianPhanHoi(new Date());
        phanHoi.setDoiTuongSuDung(userRepository.findById(idKhachHang).orElse(null));
        phanHoi.setDanhGia(reviewRepository.findById(idDanhGia).orElse(null));

        return reviewReplyRepository.save(phanHoi);
    }

    @Override
    public PhanHoi updateReplyReview(PhanHoi phanHoiMoi, int idPhanHoi) {
        PhanHoi phanHoi = reviewReplyRepository.findById(idPhanHoi).orElse(null);
        if (phanHoi != null) {
            phanHoi.setThoiGianPhanHoi(new Date());
            phanHoi.setNoiDungPhanHoi(phanHoiMoi.getNoiDungPhanHoi());
            return reviewReplyRepository.save(phanHoi);
        }
        return null;
    }

    @Override
    public boolean deleteReplyReview(int id) {
        PhanHoi ph = reviewReplyRepository.findById(id).orElse(null);
        if (ph != null) {
            reviewReplyRepository.deleteById(id);
            return true;
        }
        return false;
    }
}