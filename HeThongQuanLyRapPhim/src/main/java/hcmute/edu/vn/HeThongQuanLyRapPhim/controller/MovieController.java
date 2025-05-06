package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DanhGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MovieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

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
        Set<Phim> danhSachPhim = movieService.getMoviesByTrangThaiPhim(TrangThaiPhim.DANG_CHIEU);
        model.addAttribute("danhSachPhim", danhSachPhim);
        return "MovieShowing";
    }


    // Lấy danh sách phim sắp chiếu
    @GetMapping("/coming-soon")
    public String getComingSoon(Model model) {
        Set<Phim> danhSachPhim = movieService.getMoviesByTrangThaiPhim(TrangThaiPhim.SAP_CHIEU);
        model.addAttribute("danhSachPhim", danhSachPhim);
        return "MovieComingSoon";
    }

    // xem trang chi tiết phim đang chiếu
    @GetMapping("/movie-detail/{id}")
    public String getNowShowingDetail(Model model, @PathVariable int id) {
        Phim phim = movieService.getPhimById(id);
        model.addAttribute("phim", phim);
        model.addAttribute("danhGia", new DanhGia());
        return "MovieDetail";
    }
}
