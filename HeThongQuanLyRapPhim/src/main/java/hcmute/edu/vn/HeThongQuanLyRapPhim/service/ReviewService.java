package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DanhGia;

public interface ReviewService {
    DanhGia addReview(DanhGia danhGia);
    DanhGia findReviewById(int id);
    DanhGia updateReview(DanhGia danhGia);
    boolean deleteReview(int id);
}
