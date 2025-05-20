package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DanhGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.MovieRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.ReviewRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, MovieRepository movieRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DanhGia addReview(DanhGia danhGia, int idPhim, int idUser) {
        // Set thông tin đánh giá
        danhGia.setThoiGianDanhGia(new Date());
        danhGia.setPhim(movieRepository.findById(idPhim).orElse(null));
        danhGia.setDoiTuongSuDung(userRepository.findById(idUser).orElse(null));

        return reviewRepository.save(danhGia);
    }

    @Override
    public DanhGia updateReview(DanhGia danhGiaMoi, int idDanhGia) {
        DanhGia danhGia = reviewRepository.findById(idDanhGia).orElse(null);
        if (danhGia != null) {
            danhGia.setThoiGianDanhGia(new Date());
            danhGia.setNoiDungDanhGia(danhGiaMoi.getNoiDungDanhGia());
            danhGia.setDiemDanhGia(danhGiaMoi.getDiemDanhGia());
            return reviewRepository.save(danhGia);
        }
        return null;
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
