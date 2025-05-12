package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhanHoi;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.ReviewReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewReplyServiceImpl implements ReviewReplyService {
    private final ReviewReplyRepository reviewReplyRepository;

    @Autowired
    public ReviewReplyServiceImpl(ReviewReplyRepository reviewReplyRepository) {
        this.reviewReplyRepository = reviewReplyRepository;
    }

    @Override
    public PhanHoi addReplyReview(PhanHoi phanHoi) {
        return reviewReplyRepository.save(phanHoi);
    }

    @Override
    public PhanHoi findReplyReviewById(int id) {
        return reviewReplyRepository.findById(id).orElse(null);
    }

    @Override
    public PhanHoi updateReplyReview(PhanHoi phanHoi) {
        return reviewReplyRepository.save(phanHoi);
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