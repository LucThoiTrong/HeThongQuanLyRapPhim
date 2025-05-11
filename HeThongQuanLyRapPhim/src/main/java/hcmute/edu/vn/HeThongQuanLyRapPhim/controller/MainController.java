package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller("/")
public class MainController {
    private final MovieService movieService;

    @Autowired
    public MainController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/")
    public String MainPage(Model model) {
        // Lấy danh sách phim đang chiếu
        List<Phim> danhSachPhimDangChieu = movieService.getMoviesByTrangThaiPhim(TrangThaiPhim.DANG_CHIEU);
        // Lấy danh sách phim sắp chiếu
        List<Phim> danhSachPhimSapChiieu = movieService.getMoviesByTrangThaiPhim(TrangThaiPhim.SAP_CHIEU);
        model.addAttribute("danhSachPhimDangChieu", danhSachPhimDangChieu);
        model.addAttribute("danhSachPhimSapChieu", danhSachPhimSapChiieu);
        return "HomePage";
    }
}
