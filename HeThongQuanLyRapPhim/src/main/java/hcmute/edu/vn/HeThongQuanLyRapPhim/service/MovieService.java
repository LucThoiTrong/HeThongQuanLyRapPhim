package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiPhim;

import java.util.List;

public interface MovieService {
    List<Phim> getMoviesByTrangThaiPhim(TrangThaiPhim trangThaiPhim);
    Phim getPhimById(int id);
    List<Phim> getAllMovies();
    Phim getMovieById(int id);
    Phim createMovie(Phim phim);
    Phim updateMovie(int id, Phim Phim);
    boolean deleteMovieById(int id);
}
