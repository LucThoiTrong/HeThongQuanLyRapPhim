package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhanHoi;

public interface ReviewReplyService {
    PhanHoi addReplyReview(PhanHoi phanHoi);
    PhanHoi findReplyReviewById(int id);
    PhanHoi updateReplyReview(PhanHoi phanHoi);
    boolean deleteReplyReview(int id);
}
