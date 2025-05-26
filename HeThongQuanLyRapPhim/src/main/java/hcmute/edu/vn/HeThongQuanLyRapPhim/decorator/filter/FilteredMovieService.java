package hcmute.edu.vn.HeThongQuanLyRapPhim.decorator.filter;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoTuoi;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MovieService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.decorator.MovieServiceDecorator;

import java.util.List;
import java.util.stream.Collectors;

public class FilteredMovieService extends MovieServiceDecorator {
    private final TrangThaiPhim trangThaiPhim;
    private final DoTuoi doTuoi;

    public FilteredMovieService(MovieService decorated, TrangThaiPhim trangThaiPhim, DoTuoi doTuoi) {
        super(decorated);
        this.trangThaiPhim = trangThaiPhim;
        this.doTuoi = doTuoi;
    }

    @Override
    public List<Phim> getAllMovies() {
        return super.getAllMovies().stream()
                .filter(phim -> trangThaiPhim == null || phim.getTrangThaiPhim() == trangThaiPhim)
                .filter(phim -> doTuoi == null || phim.getDoTuoi() == doTuoi)
                .collect(Collectors.toList());
    }
}
