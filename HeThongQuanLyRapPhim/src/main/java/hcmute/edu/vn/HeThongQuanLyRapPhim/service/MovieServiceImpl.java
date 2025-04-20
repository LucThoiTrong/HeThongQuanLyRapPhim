package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Set<Phim> getMoviesByTrangThaiPhim(TrangThaiPhim trangThaiPhim) {
        return movieRepository.findByTrangThaiPhim(trangThaiPhim);
    }

    @Override
    public Phim getPhimById(int id) {
        return movieRepository.findById(id).get();
    }
}
