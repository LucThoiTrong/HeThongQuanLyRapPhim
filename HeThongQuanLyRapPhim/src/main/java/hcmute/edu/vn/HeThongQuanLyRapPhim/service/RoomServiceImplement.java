package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImplement implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<PhongChieuPhim> getAllRoomsByCinemaId(int idRapPhim) {
        List<PhongChieuPhim> rooms = roomRepository.findAllRoomByIdCinema(idRapPhim);
        System.out.println("Rooms for idRapPhim " + idRapPhim + ": " + rooms);
        return rooms;
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
    public boolean deleteRoom(int id) {
        Optional<PhongChieuPhim> optionalPhongChieuPhim = roomRepository.findById(id);
        if (optionalPhongChieuPhim.isPresent()) {
            roomRepository.deleteById(id);
            return true;
        }
        return false;
    }
}