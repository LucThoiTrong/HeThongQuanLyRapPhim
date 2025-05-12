package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DanhGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhanHoi;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.DoiTuongSuDungService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.ReviewReplyService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
@RequestMapping("/reviews")
public class ReviewReplyController {
    private final ReviewReplyService reviewReplyService;
    private final DoiTuongSuDungService doiTuongSuDungService;
    private final ReviewService reviewService;
    @Autowired
    public ReviewReplyController(ReviewReplyService reviewReplyService, DoiTuongSuDungService doiTuongSuDungService, ReviewService reviewService) {
        this.reviewReplyService = reviewReplyService;
        this.doiTuongSuDungService = doiTuongSuDungService;
        this.reviewService = reviewService;
    }

    @PostMapping("/{reviewId}/replies/new")
    public String addReplyReview(@PathVariable("reviewId") int idDanhGia,
                              @ModelAttribute("phanHoi") PhanHoi phanHoi,
                              @RequestParam("movieIdForRedirect") int idPhim,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        // Kiểm tra đăng nhập
        int idNguoiDung = (int) session.getAttribute("idCustomer");

        // Gán dữ liệu cần thiết
        DanhGia danhGia = reviewService.findReviewById(idDanhGia);
        DoiTuongSuDung nguoiDung = doiTuongSuDungService.getDoiTuongSuDungById(idNguoiDung);

        phanHoi.setDanhGia(danhGia);
        phanHoi.setDoiTuongSuDung(nguoiDung);
        phanHoi.setThoiGianPhanHoi(new Date());

        // Lưu phản hồi
        PhanHoi result = reviewReplyService.addReplyReview(phanHoi);

        // Thông báo và chuyển hướng
        if (result != null) {
            redirectAttributes.addFlashAttribute("success", "Phản hồi đã được gửi.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Gửi phản hồi thất bại.");
        }

        return "redirect:/movies/movie-detail/" + idPhim;
    }

    @PostMapping("/replies/{replyId}/update")
    public String updateReply(@PathVariable("replyId") int replyId,
                              @ModelAttribute("phanHoi") PhanHoi phanHoiMoi,
                              @RequestParam("movieIdForRedirect") int idPhim,
                              RedirectAttributes redirectAttributes) {
        // Lấy phản hồi hiện tại
        PhanHoi phanHoiCu = reviewReplyService.findReplyReviewById(replyId);
        if (phanHoiCu == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy phản hồi cần cập nhật.");
            return "redirect:/movies/movie-detail/" + idPhim;
        }

        // Cập nhật nội dung
        phanHoiCu.setNoiDungPhanHoi(phanHoiMoi.getNoiDungPhanHoi());
        phanHoiCu.setThoiGianPhanHoi(new Date());

        PhanHoi phanHoi = reviewReplyService.updateReplyReview(phanHoiCu);
        if (phanHoi != null) {
            redirectAttributes.addFlashAttribute("success", "Cập nhật phản hồi thành công.");
        } else {
            redirectAttributes.addFlashAttribute("success", "Cập nhật phản hồi thất bại.");
        }
        return "redirect:/movies/movie-detail/" + idPhim;
    }

    @PostMapping("/replies/{replyId}/delete")
    public String deleteReply(@PathVariable("replyId") int replyId,
                              @RequestParam("movieIdForRedirect") int idPhim,
                              RedirectAttributes redirectAttributes) {

        boolean isDeleted = reviewReplyService.deleteReplyReview(replyId);
        if (isDeleted) {
            redirectAttributes.addFlashAttribute("success", "Phản hồi đã được xóa.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Xóa phản hồi thất bại");
        }
        return "redirect:/movies/movie-detail/" + idPhim;
    }
}