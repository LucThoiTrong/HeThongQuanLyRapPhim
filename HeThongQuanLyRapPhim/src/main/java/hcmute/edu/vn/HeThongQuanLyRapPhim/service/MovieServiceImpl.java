package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        return movieRepository.save(phim);
    }

    @Override
    public Phim updateMovie(int id, Phim phimMoi) {
        Phim phimCu = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phim không tồn tại với ID: " + id));
        phimCu.setTenPhim(phimMoi.getTenPhim());
        phimCu.setDaoDien(phimMoi.getDaoDien());
        phimCu.setDienVien(phimMoi.getDienVien());
        phimCu.setMoTaPhim(phimMoi.getMoTaPhim());
        phimCu.setTheLoai(phimMoi.getTheLoai());
        phimCu.setThoiGianKhoiChieu(phimMoi.getThoiGianKhoiChieu());
        phimCu.setThoiLuongChieu(phimMoi.getThoiLuongChieu());
        phimCu.setLinkTrailer(phimMoi.getLinkTrailer());
        phimCu.setLinkAnh(phimMoi.getLinkAnh());
        phimCu.setDoTuoi(phimMoi.getDoTuoi());
        phimCu.setTrangThaiPhim(phimMoi.getTrangThaiPhim());
        phimCu.setHinhThucChieu(phimMoi.getHinhThucChieu());
        phimCu.setNgonNgu(phimMoi.getNgonNgu());
        return movieRepository.save(phimCu);
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

