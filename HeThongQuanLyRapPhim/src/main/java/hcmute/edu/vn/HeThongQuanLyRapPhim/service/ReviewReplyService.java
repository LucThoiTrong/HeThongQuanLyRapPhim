package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhanHoi;

public interface ReviewReplyService {
    PhanHoi addReplyReview(PhanHoi phanHoi, int idDanhGia, int idUser);
    PhanHoi updateReplyReview(PhanHoi phanHoiMoi, int idPhanHoi);
    boolean deleteReplyReview(int id);
}
