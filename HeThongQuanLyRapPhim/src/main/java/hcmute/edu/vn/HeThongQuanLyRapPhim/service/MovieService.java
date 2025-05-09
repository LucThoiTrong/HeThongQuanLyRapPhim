package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiPhim;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MovieService {
    Set<Phim> getMoviesByTrangThaiPhim(TrangThaiPhim trangThaiPhim);
    Phim getPhimById(int id);
    List<Phim> getAllMovies();
    Phim getMovieById(int id);
    Phim createMovie(Phim phim);
    Phim updateMovie(int id, Phim Phim);
    boolean deleteMovieById(int id);
    Optional<Phim> findByMovieName(String tenPhim);
}
