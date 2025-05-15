package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.*;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.BookingService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.RowSeatService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/so-do-ghe")
public class RowSeatController {
    private final RowSeatService gheService;
    private final BookingService bookingService;
    @Autowired
    public RowSeatController(RowSeatService seatService, BookingService bookingService) {
        this.gheService = seatService;
        this.bookingService = bookingService;
    }
    @GetMapping("/chon-ghe")
    public String hienThiSoDoGhe(@RequestParam ("idSuatChieu") int idSuatChieu,
                                 Model model, HttpSession session) {
        SuatChieu suatChieu = bookingService.findById(idSuatChieu);
        session.setAttribute("suatChieu", suatChieu);
        //lay ds ghe cua hoa don da thanh toan
        Set<Ghe> danhSachGheDaDat= suatChieu.getDanhSachGheDaDat();
        //tim danh sach day ghe cua phong chieu do de hien giao dien
        List<DayGhe> danhSachDayGhe = gheService.findByPhongChieuPhim(suatChieu.getPhongChieuPhim());
        model.addAttribute("danhSachDayGhe", danhSachDayGhe);
        model.addAttribute("danhSachGheDaDat", danhSachGheDaDat);
        return "SeatingPlanPage";
    }
}