package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoTuoi;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HinhThucChieu;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MovieService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/")
    public String showList(Model model, HttpSession session) {
        List<Phim> dsPhim = movieService.getAllMovies();
        session.setAttribute("movies", dsPhim);
        model.addAttribute("movies", dsPhim);
        return "MovieListPage";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("phim", new Phim());
        model.addAttribute("doTuoiList", Arrays.asList(DoTuoi.values()));
        model.addAttribute("trangThaiPhimList", Arrays.asList(TrangThaiPhim.values()));
        model.addAttribute("hinhThucChieuList", Arrays.asList(HinhThucChieu.values()));
        return "AddMovie";
    }

    @PostMapping("/new")
    public String insertPhim(@Valid @ModelAttribute("phim") Phim phim, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("doTuoiList", Arrays.asList(DoTuoi.values()));
            model.addAttribute("trangThaiPhimList", Arrays.asList(TrangThaiPhim.values()));
            model.addAttribute("hinhThucChieuList", Arrays.asList(HinhThucChieu.values()));
            return "AddMovie";
        }
        Phim p = movieService.createMovie(phim);
        if (p != null) {
            redirectAttributes.addFlashAttribute("message", "Thêm phim thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Thêm phim thất bại");
        }
        return "redirect:/movies/";
    }

    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Phim> dsPhim = (List<Phim>) session.getAttribute("movies");
        Phim phim;
        if (dsPhim == null) {
            phim = movieService.getMovieById(id);
        } else {
            phim = dsPhim.stream()
                    .filter(p -> p.getIdPhim() == id)
                    .findFirst().orElse(null);
        }
        if (phim == null) {
            return "redirect:/movies/";
        }

        model.addAttribute("phim", phim);
        model.addAttribute("doTuoiList", Arrays.asList(DoTuoi.values()));
        model.addAttribute("trangThaiPhimList", Arrays.asList(TrangThaiPhim.values()));
        model.addAttribute("hinhThucChieuList", Arrays.asList(HinhThucChieu.values()));
        return "EditMovie";
    }

    @PostMapping("/update/{id}")
    public String updatePhim(@Valid @ModelAttribute("phim") Phim phim, BindingResult result, @PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("doTuoiList", Arrays.asList(DoTuoi.values()));
            model.addAttribute("trangThaiPhimList", Arrays.asList(TrangThaiPhim.values()));
            model.addAttribute("hinhThucChieuList", Arrays.asList(HinhThucChieu.values()));
            return "EditMovie";
        }
        Phim p = movieService.updateMovie(id, phim);
        if (p != null) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật phim thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Cập nhật phim thất bại");
        }
        return "redirect:/movies/";
    }

    @PostMapping("/delete/{id}")
    public String deleteCinema(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        boolean isSuccess = movieService.deleteMovieById(id);
        if (isSuccess) {
            redirectAttributes.addFlashAttribute("message", "Xóa phim thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Xóa phim thất bại");
        }
        return "redirect:/movies/";
    }
}