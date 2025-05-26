package hcmute.edu.vn.HeThongQuanLyRapPhim.decorator.filter;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MovieService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.decorator.MovieServiceDecorator;

import java.util.List;
import java.util.stream.Collectors;

public class FilteredMovieService extends MovieServiceDecorator {
    private final TrangThaiPhim trangThaiPhim;

    public FilteredMovieService(MovieService decorated, TrangThaiPhim trangThaiPhim) {
        super(decorated);
        this.trangThaiPhim = trangThaiPhim;
    }

    @Override
    public List<Phim> getAllMovies() {
        return super.getAllMovies().stream()
                .filter(phim -> phim.getTrangThaiPhim() == trangThaiPhim)
                .collect(Collectors.toList());
    }
}