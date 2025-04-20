package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiPhim;

import java.util.Set;

public interface MovieService {
    Set<Phim> getMoviesByTrangThaiPhim(TrangThaiPhim trangThaiPhim);
    Phim getPhimById(int id);
}
