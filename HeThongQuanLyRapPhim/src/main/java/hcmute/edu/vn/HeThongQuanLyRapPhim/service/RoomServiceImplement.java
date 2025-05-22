package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.CinemaRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImplement implements RoomService {
    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;

    @Autowired
    public RoomServiceImplement(RoomRepository roomRepository, CinemaRepository cinemaRepository) {
        this.roomRepository = roomRepository;
        this.cinemaRepository = cinemaRepository;
    }

    // Thực hiện kiểm tra tên phòng chiếu phim có tồn tại trong rạp phim chưa
    @Override
    public boolean checkRoomName(String tenPhongChieuPhim, int idRapPhim) {
        // Lấy rạp phim
        RapPhim rapPhim = cinemaRepository.findById(idRapPhim).orElse(null);
        if (rapPhim != null) {
            // Thực hiện kiểm tra trong rạp phim đó có tồn tại tên phòng chiếu phim thêm mới hay không
            return rapPhim.kiemTraTenRapPhim(tenPhongChieuPhim);
        }
        return false;
    }

    @Override
    public RapPhim getCinemaById(int idRapPhim) {
        return cinemaRepository.findById(idRapPhim).orElse(null);
    }

    @Override
    public List<PhongChieuPhim> getAllRoomsByCinemaId(int idRapPhim) {
        RapPhim rapPhim = cinemaRepository.findById(idRapPhim).orElse(null);
        if (rapPhim != null) {
            return roomRepository.findAllByRapPhim(rapPhim);
        }
        return null;
    }

    @Override
    public PhongChieuPhim getRoomById(int id) {
        return roomRepository.findById(id).orElse(null);
    }

    @Override
    public PhongChieuPhim createRoom(PhongChieuPhim phongChieuPhim) {
        return roomRepository.save(phongChieuPhim);
    }

    @Override
    public PhongChieuPhim updateRoom(int id, PhongChieuPhim phongChieuPhimMoi) {
        PhongChieuPhim phongChieuPhimCu = roomRepository.findById(id).orElse(null);
        if (phongChieuPhimCu != null) {
            phongChieuPhimCu.setTenPhongChieuPhim(phongChieuPhimMoi.getTenPhongChieuPhim());
            phongChieuPhimCu.setKichThuocPhong(phongChieuPhimMoi.getKichThuocPhong());
            phongChieuPhimCu.setRapPhim(phongChieuPhimMoi.getRapPhim());
            return roomRepository.save(phongChieuPhimCu);
        }
        return null;
    }

    @Override
    public boolean deleteRoom(int idPhongChieuPhim) {
        PhongChieuPhim phongChieuPhim = roomRepository.findById(idPhongChieuPhim).orElse(null);
        if (phongChieuPhim != null) {
           roomRepository.deleteById(idPhongChieuPhim);
           return true;
        }
        return false;
    }

    @Override
    public PhongChieuPhim getPhongChieuPhimByName(String name) {
        return roomRepository.findByTenPhongChieuPhim(name);
    }
}