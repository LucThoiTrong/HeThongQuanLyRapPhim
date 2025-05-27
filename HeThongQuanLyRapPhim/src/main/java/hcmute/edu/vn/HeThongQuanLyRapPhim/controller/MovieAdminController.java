package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.decorator.filter.FilteredMovieService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.decorator.pagination.PaginatedMovieService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.decorator.sort.SortedMovieService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.facade.MovieSearchFacade;
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

    private static final String MOVIE_PAGE = "MoviePage";
    private static final String ADD_MOVIE_PAGE = "AddMoviePage";
    private static final String EDIT_MOVIE_PAGE = "EditMoviePage";
    private static final String SUCCESS_MESSAGE = "message";
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final MovieService movieService;
    private final MovieSearchFacade movieSearchFacade;

    @Autowired
    public MovieAdminController(MovieService movieService, MovieSearchFacade movieSearchFacade) {
        this.movieService = movieService;
        this.movieSearchFacade = movieSearchFacade;
    }

    private void prepareFormModel(Model model) {
        model.addAttribute("doTuoiList", Arrays.asList(DoTuoi.values()));
        model.addAttribute("trangThaiPhimList", Arrays.asList(TrangThaiPhim.values()));
        model.addAttribute("hinhThucChieuList", Arrays.asList(HinhThucChieu.values()));
    }

    private List<Phim> filterMovies(List<Phim> movies, TrangThaiPhim trangThaiPhim, DoTuoi doTuoi) {
        return movies.stream()
                .filter(phim -> trangThaiPhim == null || phim.getTrangThaiPhim() == trangThaiPhim)
                .filter(phim -> doTuoi == null || phim.getDoTuoi() == doTuoi)
                .toList();
    }

    private void processMovies(List<Phim> movies, int page, int size, TrangThaiPhim trangThaiPhim,
                               DoTuoi doTuoi, String sortBy, boolean ascending, String keyword, Model model) {
        // Kiểm tra giá trị đầu vào
        if (page < 0) page = 0;
        if (size <= 0) size = DEFAULT_PAGE_SIZE;

        // B1: Lọc
        List<Phim> filteredMovies = filterMovies(movies, trangThaiPhim, doTuoi);
        MovieService service = new FilteredMovieService(movieService, trangThaiPhim, doTuoi) {
            @Override
            public List<Phim> getAllMovies() {
                return filteredMovies;
            }
        };

        // B2: Sắp xếp
        if (sortBy != null && !sortBy.isEmpty()) {
            service = new SortedMovieService(service, sortBy, ascending);
        }

        // B3: Phân trang
        service = new PaginatedMovieService(service, page, size);
        List<Phim> dsPhim = service.getAllMovies();

        // Tính tổng số trang
        int totalFilteredMovies = filteredMovies.size();
        int maxPage = Math.max(0, (totalFilteredMovies + size - 1) / size - 1);
        if (page > maxPage) page = maxPage;

        // Thêm các thuộc tính vào model
        model.addAttribute("movies", dsPhim);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("maxPage", maxPage);
        model.addAttribute("doTuoiList", Arrays.asList(DoTuoi.values()));
        model.addAttribute("trangThaiPhimList", Arrays.asList(TrangThaiPhim.values()));
        model.addAttribute("selectedTrangThaiPhim", trangThaiPhim);
        model.addAttribute("selectedDoTuoi", doTuoi);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("ascending", ascending);
        model.addAttribute("keyword", keyword);
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

        // Lấy toàn bộ danh sách phim
        List<Phim> movies = movieService.getAllMovies();

        // Xử lý danh sách phim
        processMovies(movies, page, size, trangThaiPhim, doTuoi, sortBy, ascending, null, model);

        return MOVIE_PAGE;
    }

    @GetMapping("/search")
    public String searchMovies(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) TrangThaiPhim trangThaiPhim,
            @RequestParam(required = false) DoTuoi doTuoi,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(required = false) String keyword) {

        // Tìm kiếm phim bằng MovieSearchFacade
        List<Phim> movies = (keyword != null && !keyword.trim().isEmpty())
                ? movieSearchFacade.timKiemPhim(keyword)
                : movieService.getAllMovies();

        // Xử lý danh sách phim
        processMovies(movies, page, size, trangThaiPhim, doTuoi, sortBy, ascending, keyword, model);

        return MOVIE_PAGE;
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("phim", new Phim());
        prepareFormModel(model);
        return ADD_MOVIE_PAGE;
    }

    @PostMapping("/new")
    public String insertPhim(
            @ModelAttribute("phim") Phim phim,
            Model model,
            RedirectAttributes redirectAttributes) {

        Phim created = movieService.createMovie(phim);
        if (created == null) {
            model.addAttribute("phim", phim);
            prepareFormModel(model);
            model.addAttribute(SUCCESS_MESSAGE, "Tên phim đã tồn tại");
            return ADD_MOVIE_PAGE;
        }

        redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Thêm phim thành công");
        return "redirect:/movies/";
    }

    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Phim phim = movieService.getMovieById(id);
        if (phim == null) {
            model.addAttribute(SUCCESS_MESSAGE, "Phim không tồn tại");
            return MOVIE_PAGE;
        }
        model.addAttribute("phim", phim);
        prepareFormModel(model);
        return EDIT_MOVIE_PAGE;
    }

    @PostMapping("/update/{id}")
    public String updatePhim(
            @ModelAttribute("phim") Phim phim,
            @PathVariable int id,
            RedirectAttributes redirectAttributes) {
        Phim updated = movieService.updateMovie(id, phim);
        redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE,
                updated == null ? "Cập nhật phim thất bại" : "Cập nhật phim thành công");
        return "redirect:/movies/";
    }

    @PostMapping("/delete/{id}")
    public String deleteMovie(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        boolean isSuccess = movieService.deleteMovieById(id);
        redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE,
                isSuccess ? "Xóa phim thành công" : "Xóa phim thất bại");
        return "redirect:/movies/";
    }
}