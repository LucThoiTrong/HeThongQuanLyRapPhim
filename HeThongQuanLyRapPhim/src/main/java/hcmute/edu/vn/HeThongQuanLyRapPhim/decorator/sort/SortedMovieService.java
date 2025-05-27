package hcmute.edu.vn.HeThongQuanLyRapPhim.decorator.sort;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MovieService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.decorator.MovieServiceDecorator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortedMovieService extends MovieServiceDecorator {
    private final String sortBy;
    private final boolean ascending;

    public SortedMovieService(MovieService decorated, String sortBy, boolean ascending) {
        super(decorated);
        this.sortBy = sortBy;
        this.ascending = ascending;
    }

    @Override
    public List<Phim> getAllMovies() {
        Comparator<Phim> comparator = switch (sortBy) {
            case "tenPhim" -> Comparator.comparing(Phim::getTenPhim, String.CASE_INSENSITIVE_ORDER);
            case "daoDien" -> Comparator.comparing(Phim::getDaoDien, String.CASE_INSENSITIVE_ORDER);
            case "theLoai" -> Comparator.comparing(Phim::getTheLoai, String.CASE_INSENSITIVE_ORDER);
            case "khoiChieu" -> Comparator.comparing(Phim::getThoiGianKhoiChieu);
            case "thoiLuong" -> Comparator.comparing(Phim::getThoiLuongChieu);
            case "doTuoi" -> Comparator.comparing(phim -> phim.getDoTuoi().ordinal());
            case "trangThaiPhim" -> Comparator.comparing(phim -> phim.getTrangThaiPhim().ordinal());
            default -> Comparator.comparing(Phim::getIdPhim);
        };

        if (!ascending) {
            comparator = comparator.reversed();
        }

        return super.getAllMovies().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
