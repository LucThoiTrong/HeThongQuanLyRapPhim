package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.decorator.filter.FilteredMovieService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.decorator.pagination.PaginatedMovieService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.decorator.sort.SortedMovieService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.*;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieAdminController {
    private final MovieService movieService;

    private void prepareFormModel(Model model) {
        model.addAttribute("doTuoiList", Arrays.asList(DoTuoi.values()));
        model.addAttribute("trangThaiPhimList", Arrays.asList(TrangThaiPhim.values()));
        model.addAttribute("hinhThucChieuList", Arrays.asList(HinhThucChieu.values()));
    }

    @Autowired
    public MovieAdminController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping({"", "/"})
    public String showList(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) TrangThaiPhim trangThaiPhim,
            @RequestParam(required = false) DoTuoi doTuoi,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {
        MovieService service = movieService;

        // B1: Lọc
        service = new FilteredMovieService(service, trangThaiPhim, doTuoi);

        // B2: Sắp xếp
        if (sortBy != null && !sortBy.isEmpty()) {
            service = new SortedMovieService(service, sortBy, ascending);
        }

        // B3: Phân trang
        service = new PaginatedMovieService(service, page, size);

        List<Phim> dsPhim = service.getAllMovies();

        // Tính tổng số trang sau khi lọc (trước khi phân trang)
        int totalFilteredMovies = new FilteredMovieService(movieService, trangThaiPhim, doTuoi).getAllMovies().size();
        int maxPage = (totalFilteredMovies + size - 1) / size - 1;

        model.addAttribute("movies", dsPhim);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("maxPage", maxPage);

        // Phục vụ hiển thị form
        model.addAttribute("doTuoiList", Arrays.asList(DoTuoi.values()));
        model.addAttribute("trangThaiPhimList", Arrays.asList(TrangThaiPhim.values()));
        model.addAttribute("selectedTrangThaiPhim", trangThaiPhim);
        model.addAttribute("selectedDoTuoi", doTuoi);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("ascending", ascending);

        return "MoviePage";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("phim", new Phim());
        prepareFormModel(model);
        return "AddMoviePage";
    }

    @PostMapping("/new")
    public String insertPhim(@ModelAttribute("phim") Phim phim,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        Phim created = movieService.createMovie(phim);
        if (created == null) {
            model.addAttribute("phim", phim);
            prepareFormModel(model);
            model.addAttribute("message", "Tên phim đã tồn tại");
            return "AddMoviePage";
        }
        redirectAttributes.addFlashAttribute("message", "Thêm phim thành công");
        return "redirect:/movies/";
    }

    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Phim phim = movieService.getMovieById(id);
        model.addAttribute("phim", phim);
        prepareFormModel(model);
        return "EditMoviePage";
    }

    @PostMapping("/update/{id}")
    public String updatePhim(@ModelAttribute("phim") Phim phim,
                             @PathVariable int id,
                             RedirectAttributes redirectAttributes) {
        Phim updated = movieService.updateMovie(id, phim);
        if(updated == null) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật phim thất bại");
        } else {
            redirectAttributes.addFlashAttribute("message", "Cập nhật phim thành công");
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

    @GetMapping("/search")
    public String searchMovies(@RequestParam("keyword") String keyword, Model model) {
        List<Phim> ketQua = movieService.searchMovies(keyword);
        model.addAttribute("movies", ketQua);
        return "MoviePage";
    }
}