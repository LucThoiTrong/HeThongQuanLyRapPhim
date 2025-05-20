package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhanHoi;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.ReviewReplyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reviews")
public class ReviewReplyController {
    private final ReviewReplyService reviewReplyService;
    @Autowired
    public ReviewReplyController(ReviewReplyService reviewReplyService) {
        this.reviewReplyService = reviewReplyService;
    }

    @PostMapping("/{reviewId}/replies/new")
    public String addReplyReview(@PathVariable("reviewId") int idDanhGia,
                              @ModelAttribute("phanHoi") PhanHoi phanHoi,
                              @RequestParam("movieIdForRedirect") int idPhim,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        // Lấy idNguoiDung
        int idNguoiDung = (int) session.getAttribute("idCustomer");

        // Lưu phản hồi
        PhanHoi result = reviewReplyService.addReplyReview(phanHoi, idDanhGia, idNguoiDung);

        // Thông báo và chuyển hướng
        if (result != null) {
            redirectAttributes.addFlashAttribute("message", "Gửi phản hồi thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Gửi phản hồi thất bại.");
        }

        return "redirect:/movies/movie-detail/" + idPhim;
    }

    @PostMapping("/replies/{replyId}/update")
    public String updateReply(@PathVariable("replyId") int replyId,
                              @ModelAttribute("phanHoi") PhanHoi phanHoiMoi,
                              @RequestParam("movieIdForRedirect") int idPhim,
                              RedirectAttributes redirectAttributes) {
        PhanHoi phanHoi = reviewReplyService.updateReplyReview(phanHoiMoi, replyId);
        if (phanHoi != null) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật phản hồi thành công.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Cập nhật phản hồi thất bại.");
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