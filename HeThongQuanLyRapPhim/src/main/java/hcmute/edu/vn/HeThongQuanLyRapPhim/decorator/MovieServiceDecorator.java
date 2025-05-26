package hcmute.edu.vn.HeThongQuanLyRapPhim.decorator;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MovieService;

import java.util.List;

public abstract class MovieServiceDecorator implements MovieService {
    protected final MovieService decorated;

    protected MovieServiceDecorator(MovieService decorated) {
        this.decorated = decorated;
    }

    @Override
    public List<Phim> getMoviesByTrangThaiPhim(TrangThaiPhim trangThaiPhim) {
        return decorated.getMoviesByTrangThaiPhim(trangThaiPhim);
    }

    @Override
    public Phim getPhimById(int id) {
        return decorated.getPhimById(id);
    }

    @Override
    public List<Phim> getAllMovies() {
        return decorated.getAllMovies();
    }

    @Override
    public Phim getMovieById(int id) {
        return decorated.getMovieById(id);
    }

    @Override
    public Phim createMovie(Phim phim) {
        return decorated.createMovie(phim);
    }

    @Override
    public Phim updateMovie(int id, Phim phim) {
        return decorated.updateMovie(id, phim);
    }

    @Override
    public boolean deleteMovieById(int id) {
        return decorated.deleteMovieById(id);
    }

    @Override
    public List<Phim> searchMovies(String keyword) {
        return decorated.searchMovies(keyword);
    }
}
