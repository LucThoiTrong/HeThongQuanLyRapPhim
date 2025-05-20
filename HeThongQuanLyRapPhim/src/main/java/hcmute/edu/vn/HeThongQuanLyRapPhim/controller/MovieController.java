package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.*;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // Lấy danh sách phim đang chiếu
    @GetMapping("/now-showing")
    public String getNowShowing(Model model) {
        List<Phim> danhSachPhim = movieService.getMoviesByTrangThaiPhim(TrangThaiPhim.DANG_CHIEU);
        model.addAttribute("danhSachPhim", danhSachPhim);
        return "MovieShowingPage";
    }


    // Lấy danh sách phim sắp chiếu
    @GetMapping("/coming-soon")
    public String getComingSoon(Model model) {
        List<Phim> danhSachPhim = movieService.getMoviesByTrangThaiPhim(TrangThaiPhim.SAP_CHIEU);
        model.addAttribute("danhSachPhim", danhSachPhim);
        return "MovieComingSoonPage";
    }

    // xem trang chi tiết phim đang chiếu
    @GetMapping("/movie-detail/{id}")
    public String getShowingDetail(Model model, @PathVariable int id) {
        Phim phim = movieService.getPhimById(id);

        model.addAttribute("phim", phim);
        model.addAttribute("danhGia", new DanhGia());
        model.addAttribute("phanHoi", new PhanHoi());
        return "MovieDetailPage";
    }
}
