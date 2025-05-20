package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DanhGia;

public interface ReviewService {
    DanhGia addReview(DanhGia danhGia, int idPhim, int idUser);
    DanhGia updateReview(DanhGia danhGiaNew, int idDanhGia);
    boolean deleteReview(int id);
}
