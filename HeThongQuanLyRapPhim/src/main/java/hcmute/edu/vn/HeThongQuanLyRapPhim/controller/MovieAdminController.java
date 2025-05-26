package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.decorator.pagination.PaginatedMovieService;
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

    private MovieService getDecoratedService(int page, int size) {
        // Bắt đầu từ service gốc
        MovieService service = movieService;

        // TODO: Nếu cần filter/sort, bạn có thể bọc thêm decorator ở đây
        // Ví dụ: service = new FilteredMovieService(service, ...);
        // service = new SortedMovieService(service, ...);

        // Bọc phân trang decorator
        service = new PaginatedMovieService(service, page, size);

        return service;
    }

    @GetMapping({"", "/"})
    public String showList(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        MovieService service = movieService;
        service = new PaginatedMovieService(service, page, size);

        List<Phim> dsPhim = service.getAllMovies();

        // Tổng phim không phân trang (để tính maxPage)
        int totalMovies = movieService.getAllMovies().size();
        int maxPage = (totalMovies + size - 1) / size - 1;

        model.addAttribute("movies", dsPhim);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("maxPage", maxPage);

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
    public String searchMovies(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        MovieService service = getDecoratedService(page, size);
        List<Phim> ketQua = service.searchMovies(keyword);

        model.addAttribute("movies", ketQua);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("keyword", keyword);

        return "MoviePage";
    }
}