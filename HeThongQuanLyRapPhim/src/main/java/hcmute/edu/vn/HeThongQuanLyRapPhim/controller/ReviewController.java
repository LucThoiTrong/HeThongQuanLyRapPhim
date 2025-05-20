package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DanhGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/movies")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{movieId}/reviews")
    public String submitReview(@PathVariable("movieId") int movieId, @ModelAttribute("danhGia") DanhGia danhGia, RedirectAttributes redirectAttributes, HttpSession session) {
        // Lấy id khách hàng
        int idCustomer = (int) session.getAttribute("idCustomer");

        DanhGia kq = reviewService.addReview(danhGia, movieId, idCustomer);
        if (kq != null) {
            redirectAttributes.addFlashAttribute("message", "Thêm đánh giá thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Thêm đánh giá thất bại");
        }
        return "redirect:/movies/movie-detail/" + movieId;
    }

    // update
    @PostMapping("/{movieId}/reviews/{reviewId}/update")
    public String updateReview(@PathVariable("movieId") int movieId,
                               @PathVariable("reviewId") int reviewId,
                               @ModelAttribute("danhGia") DanhGia danhGiaMoi,
                               RedirectAttributes redirectAttributes) {
        // Lưu lại đánh giá đã được cập nhật
        DanhGia updatedReview = reviewService.updateReview(danhGiaMoi, reviewId);

        if (updatedReview != null) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật đánh giá thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Cập nhật đánh giá thất bại");
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