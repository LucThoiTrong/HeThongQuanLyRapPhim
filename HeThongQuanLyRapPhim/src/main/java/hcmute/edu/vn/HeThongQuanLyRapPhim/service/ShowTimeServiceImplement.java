package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.SuatChieu;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.CinemaRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.MovieRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.RoomRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.ShowTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowTimeServiceImplement implements ShowTimeService {
    private final ShowTimeRepository showTimeRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;

    @Autowired
    public ShowTimeServiceImplement(ShowTimeRepository showTimeRepository, MovieRepository movieRepository, RoomRepository roomRepository, CinemaRepository cinemaRepository) {
        this.showTimeRepository = showTimeRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.cinemaRepository = cinemaRepository;
    }

    // Lấy toàn bộ danh sách suất chiếu
    @Override
    public List<SuatChieu> getAllShowTimes() {
        return showTimeRepository.findAll();
    }

    @Override
    public SuatChieu getShowTimeById(int idSuatChieuPhim) {
        return showTimeRepository.findById(idSuatChieuPhim).orElse(null);
    }

    // Thực hiện lưu suất chiếu
    @Override
    public SuatChieu createShowTime(SuatChieu newShowTime) {
        return showTimeRepository.save(newShowTime);
    }

    @Override
    public SuatChieu updateShowTime(int id, SuatChieu newShowTime) {
        SuatChieu suatChieu = showTimeRepository.findById(id).orElse(null);
        if(suatChieu!=null) {
            suatChieu.setPhim(newShowTime.getPhim());
            suatChieu.setPhongChieuPhim(newShowTime.getPhongChieuPhim());
            suatChieu.setNgayGioChieu(newShowTime.getNgayGioChieu());
            suatChieu.setHinhThucChieu(newShowTime.getHinhThucChieu());
            return showTimeRepository.save(suatChieu);
        }
        return null;
    }

    @Override
    public boolean deleteShowTimeById(int id) {
        if (showTimeRepository.existsById(id)) {
            showTimeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Phim> getAllPhims() {
        return movieRepository.findAll();
    }

    @Override
    public List<RapPhim> getAllRapPhims() {
        return cinemaRepository.findAll();
    }

    @Override
    public List<PhongChieuPhim> getAllPhongChieuPhims(int idRapPhim) {
        RapPhim rapPhim = cinemaRepository.findById(idRapPhim).orElse(null);
        if (rapPhim != null) {
            return roomRepository.findAllByRapPhim(rapPhim);
        }
        return null;
    }

    @Override
    public boolean kiemTraThoiGianSuatChieu(SuatChieu suatChieu) {
        // Set up Phim
        int idPhim = suatChieu.getPhim().getIdPhim();
        Phim phim = movieRepository.findById(idPhim).orElse(null);
        suatChieu.setPhim(phim);

        // Set up PhongChieuPhim
        int idPhongChieu = suatChieu.getPhongChieuPhim().getIdPhongChieuPhim();
        PhongChieuPhim phongChieu = roomRepository.findById(idPhongChieu).orElse(null);
        suatChieu.setPhongChieuPhim(phongChieu);
        
        return suatChieu.getPhongChieuPhim().kiemTraThoiGianSuatChieu(suatChieu);
    }
}