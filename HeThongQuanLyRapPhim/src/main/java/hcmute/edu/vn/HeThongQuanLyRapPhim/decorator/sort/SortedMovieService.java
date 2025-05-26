package hcmute.edu.vn.HeThongQuanLyRapPhim.decorator.sort;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MovieService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.decorator.MovieServiceDecorator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortedMovieService extends MovieServiceDecorator {

    public SortedMovieService(MovieService decorated) {
        super(decorated);
    }

    @Override
    public List<Phim> getAllMovies() {
        return super.getAllMovies().stream()
                .sorted(Comparator.comparing(Phim::getTenPhim, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }
}
