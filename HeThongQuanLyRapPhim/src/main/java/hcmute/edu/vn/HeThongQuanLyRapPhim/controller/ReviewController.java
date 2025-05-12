package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DanhGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.DoiTuongSuDungService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MovieService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
@RequestMapping("/movies")
public class ReviewController {
    private final ReviewService reviewService;
    private final MovieService movieService;
    private final DoiTuongSuDungService doiTuongSuDungService;

    @Autowired
    public ReviewController(ReviewService reviewService, MovieService movieService, DoiTuongSuDungService doiTuongSuDungService) {
        this.reviewService = reviewService;
        this.movieService = movieService;
        this.doiTuongSuDungService = doiTuongSuDungService;
    }

    @PostMapping("/{movieId}/reviews")
    public String submitReview(@PathVariable("movieId") Integer movieId, @ModelAttribute("danhGia") DanhGia danhGia, RedirectAttributes redirectAttributes, HttpSession session) {
        // Set thời gian hiện tại và đánh giá này thuộc về phim nào
        danhGia.setThoiGianDanhGia(new Date());
        danhGia.setPhim(movieService.getPhimById(movieId));

        int idCustomer = (int) session.getAttribute("idCustomer");
        DoiTuongSuDung customer = doiTuongSuDungService.getDoiTuongSuDungById(idCustomer);

        danhGia.setDoiTuongSuDung(customer);
        DanhGia kq = reviewService.addReview(danhGia);
        if (kq != null) {
            redirectAttributes.addFlashAttribute("message", "Thêm đánh giá thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Thêm đánh giá thất bại");
        }
        return "redirect:/movies/movie-detail/" + movieId;
    }

    // update
    @PostMapping("/{movieId}/reviews/{reviewId}/update")
    public String updateReview(@PathVariable("movieId") Integer movieId,
                               @PathVariable("reviewId") Integer reviewId,
                               @ModelAttribute("danhGia") DanhGia danhGia,
                               RedirectAttributes redirectAttributes) {
        // Tìm đánh giá từ database
        DanhGia existingReview = reviewService.findReviewById(reviewId);

        if (existingReview != null) {
            // Cập nhật thông tin đánh giá
            existingReview.setNoiDungDanhGia(danhGia.getNoiDungDanhGia());
            existingReview.setDiemDanhGia(danhGia.getDiemDanhGia());
            existingReview.setThoiGianDanhGia(new Date());

            // Lưu lại đánh giá đã được cập nhật
            DanhGia updatedReview = reviewService.updateReview(existingReview);

            if (updatedReview != null) {
                redirectAttributes.addFlashAttribute("message", "Cập nhật đánh giá thành công");
            } else {
                redirectAttributes.addFlashAttribute("message", "Cập nhật đánh giá thất bại");
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "Đánh giá không tồn tại");
        }

        return "redirect:/movies/movie-detail/" + movieId;
    }

    // Xoá
    @PostMapping("/{movieId}/reviews/{reviewId}/delete")
    public String deleteReview(@PathVariable("movieId") int movieId,
                               @PathVariable("reviewId") int reviewId,
                               RedirectAttributes redirectAttributes) {
        // Xóa đánh giá từ database
        boolean isDeleted = reviewService.deleteReview(reviewId);

        if (isDeleted) {
            redirectAttributes.addFlashAttribute("message", "Xóa đánh giá thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Xóa đánh giá thất bại");
        }

        return "redirect:/movies/movie-detail/" + movieId;
    }
}