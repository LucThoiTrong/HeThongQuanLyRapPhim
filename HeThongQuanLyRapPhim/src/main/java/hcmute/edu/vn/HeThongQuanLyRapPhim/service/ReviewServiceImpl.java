package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DanhGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public DanhGia addReview(DanhGia danhGia) {
        return reviewRepository.save(danhGia);
    }

    @Override
    public DanhGia findReviewById(int id) {
        return reviewRepository.findById(id).orElse(null);
    }

    @Override
    public DanhGia updateReview(DanhGia danhGia) {
        return reviewRepository.save(danhGia);
    }

    @Override
    public boolean deleteReview(int id) {
        DanhGia dg = reviewRepository.findById(id).orElse(null);
        if (dg != null) {
            reviewRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
