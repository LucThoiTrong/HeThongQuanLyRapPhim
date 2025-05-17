package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Phim> getMoviesByTrangThaiPhim(TrangThaiPhim trangThaiPhim) {
        return movieRepository.findByTrangThaiPhim(trangThaiPhim);
    }

    @Override
    public Phim getPhimById(int id) {
        return movieRepository.findById(id).orElse(null);
    }

    @Override
    public List<Phim> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Phim getMovieById(int id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phim không tồn tại với ID: " + id));
    }

    @Override
    public Phim createMovie(Phim phim) {
        Phim existing = movieRepository.findByTenPhim(phim.getTenPhim()).orElse(null);
        if (existing != null) {
            return null; // Phim đã tồn tại, trả về null
        }
        return movieRepository.save(phim);
    }

    @Override
    public Phim updateMovie(int id, Phim phimMoi) {
        Phim existing = movieRepository.findById(id).orElse(null);
        if (existing != null) {
            Phim phimTrungTen = movieRepository.findByTenPhim(phimMoi.getTenPhim()).orElse(null);
            // Nếu tên bị trùng và ID khác thì không update
            if (phimTrungTen != null && phimTrungTen.getIdPhim() != id) {
                return null;
            }
            existing.setTenPhim(phimMoi.getTenPhim());
            existing.setDoTuoi(phimMoi.getDoTuoi());
            existing.setHinhThucChieu(phimMoi.getHinhThucChieu());
            existing.setThoiLuongChieu(phimMoi.getThoiLuongChieu());
            existing.setMoTaPhim(phimMoi.getMoTaPhim());
            existing.setTrangThaiPhim(phimMoi.getTrangThaiPhim());
            return movieRepository.save(existing);
        }
        return null;
    }


    @Override
    public boolean deleteMovieById(int id) {
        Optional<Phim> optionalPhim = movieRepository.findById(id);
        if (optionalPhim.isPresent()) {
            movieRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

