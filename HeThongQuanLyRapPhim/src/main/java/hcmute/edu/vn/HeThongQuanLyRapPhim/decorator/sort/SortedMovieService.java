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
        Comparator<Phim> comparator;

        switch (sortBy) {
            case "id":
                comparator = Comparator.comparing(Phim::getIdPhim);
                break;
            case "tenPhim":
                comparator = Comparator.comparing(Phim::getTenPhim, String.CASE_INSENSITIVE_ORDER);
                break;
            case "daoDien":
                comparator = Comparator.comparing(Phim::getDaoDien, String.CASE_INSENSITIVE_ORDER);
                break;
            case "theLoai":
                comparator = Comparator.comparing(Phim::getTheLoai, String.CASE_INSENSITIVE_ORDER);
                break;
            case "khoiChieu":
                comparator = Comparator.comparing(Phim::getThoiGianKhoiChieu);
                break;
            case "thoiLuong":
                comparator = Comparator.comparing(Phim::getThoiLuongChieu);
                break;
            case "doTuoi":
                comparator = Comparator.comparing(phim -> phim.getDoTuoi().ordinal());
                break;
            case "trangThaiPhim":
                comparator = Comparator.comparing(phim -> phim.getTrangThaiPhim().ordinal());
                break;
            default:
                comparator = Comparator.comparing(Phim::getIdPhim);
        }

        if (!ascending) {
            comparator = comparator.reversed();
        }

        return super.getAllMovies().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
